package org.example.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    
    /**
     * 接收Stripe Webhook回调
     * Receive Stripe webhook callback
     * 
     * @param payload 请求体
     * @return 响应
     */
    @PostMapping("/stripe")
    public ResponseEntity<String> handleStripeWebhook(@RequestBody String payload) {
        
        logger.info("收到Stripe Webhook回调！");
        
        return ResponseEntity.ok("success");
    }
}
