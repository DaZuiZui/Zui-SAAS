package org.example.demo.controller;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;
import org.example.demo.config.StripeConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Webhook控制器
 * Webhook Controller
 */
@RestController
@RequestMapping("/api/webhooks")
public class WebhookController {

    private static final Logger logger = LoggerFactory.getLogger(WebhookController.class);

    @Autowired
    private StripeConfig stripeConfig;

    /**
     * 接收Stripe Webhook回调（带签名验证）
     * Receive Stripe webhook callback with signature verification
     *
     * @param payload 请求体
     * @param sigHeader Stripe签名头
     * @return 响应
     */
    @PostMapping("/stripe")
    public ResponseEntity<String> handleStripeWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) {

        Event event;

        try {
            // 验证Webhook签名，防止伪造请求
            event = Webhook.constructEvent(
                    payload,
                    sigHeader,
                    stripeConfig.webhookSecret
            );

            logger.info("收到合法的Stripe Webhook回调！事件类型: {}", event.getType());

            // 事件分发
            switch (event.getType()) {
                // 用户在 Stripe Checkout 页面完成支付（无论是订阅还是一次性付款）。
                case "checkout.session.completed":
                    logger.info("处理事件: checkout.session.completed");
                    break;
                // 新订阅创建（通常和 checkout.session.completed 一起出现）。
                case "customer.subscription.created":
                    logger.info("处理事件: customer.subscription.created");
                    break;
                // 订阅变更（如升级、降级、暂停、恢复）。
                case "customer.subscription.updated":
                    logger.info("处理事件: customer.subscription.updated");
                    break;
                // 订阅被取消（用户主动取消或到期未续费）。
                case "customer.subscription.deleted":
                    logger.info("处理事件: customer.subscription.deleted");
                    break;
                // 发票支付成功（订阅首次扣款或续费时都会触发）
                case "invoice.payment_succeeded":
                    logger.info("处理事件: invoice.payment_succeeded");
                    break;
                // 发票支付失败（如用户卡片过期、余额不足）。
                case "invoice.payment_failed":
                    logger.info("处理事件: invoice.payment_failed");
                    break;
                default:
                    logger.info("收到未处理的事件类型: {}", event.getType());
            }

            return ResponseEntity.ok("success");

        } catch (SignatureVerificationException e) {
            logger.error("❌ Webhook签名验证失败！可能是伪造请求: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid signature");
        } catch (Exception e) {
            logger.error("❌ 处理Webhook失败: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Webhook error");
        }
    }
}
