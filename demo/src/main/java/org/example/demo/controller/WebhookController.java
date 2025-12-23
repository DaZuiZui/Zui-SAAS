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

            // TODO: 在这里处理不同的事件类型
            // 例如: checkout.session.completed, customer.subscription.created等

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
