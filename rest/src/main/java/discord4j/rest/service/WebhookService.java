/*
 * This file is part of Discord4J.
 *
 * Discord4J is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Discord4J is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Discord4J.  If not, see <http://www.gnu.org/licenses/>.
 */
package discord4j.rest.service;

import discord4j.discordjson.json.*;
import discord4j.rest.request.Router;
import discord4j.rest.route.Routes;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.annotation.Nullable;

public class WebhookService extends RestService {

    public WebhookService(Router router) {
        super(router);
    }

    public Mono<WebhookData> createWebhook(long channelId, WebhookCreateRequest request, @Nullable String reason) {
        return Routes.CHANNEL_WEBHOOK_CREATE.newRequest(channelId)
                .body(request)
                .optionalHeader("X-Audit-Log-Reason", reason)
                .exchange(getRouter())
                .bodyToMono(WebhookData.class);
    }

    public Flux<WebhookData> getChannelWebhooks(long channelId) {
        return Routes.CHANNEL_WEBHOOKS_GET.newRequest(channelId)
                .exchange(getRouter())
                .bodyToMono(WebhookData[].class)
                .flatMapMany(Flux::fromArray);
    }

    public Flux<WebhookData> getGuildWebhooks(long guildId) {
        return Routes.GUILD_WEBHOOKS_GET.newRequest(guildId)
                .exchange(getRouter())
                .bodyToMono(WebhookData[].class)
                .flatMapMany(Flux::fromArray);
    }

    public Mono<WebhookData> getWebhook(long webhookId) {
        return Routes.WEBHOOK_GET.newRequest(webhookId)
                .exchange(getRouter())
                .bodyToMono(WebhookData.class);
    }

    public Mono<WebhookData> modifyWebhook(long webhookId, WebhookModifyRequest request, @Nullable String reason) {
        return Routes.WEBHOOK_MODIFY.newRequest(webhookId)
                .body(request)
                .optionalHeader("X-Audit-Log-Reason", reason)
                .exchange(getRouter())
                .bodyToMono(WebhookData.class);
    }

    public Mono<Void> deleteWebhook(long webhookId, @Nullable String reason) {
        return Routes.WEBHOOK_DELETE.newRequest(webhookId)
                .optionalHeader("X-Audit-Log-Reason", reason)
                .exchange(getRouter())
                .bodyToMono(Void.class);
    }

    public Mono<MessageData> modifyWebhookMessage(long webhookId, String webhookToken, long messageId, WebhookMessageEditRequest request) {
        return Routes.WEBHOOK_MESSAGE_EDIT.newRequest(webhookId, webhookToken, messageId)
            .body(request)
            .exchange(getRouter())
            .bodyToMono(MessageData.class);
    }

    public Mono<Void> deleteWebhookMessage(long webhookId, String webhookToken, long messageId) {
        return Routes.WEBHOOK_MESSAGE_DELETE.newRequest(webhookId, webhookToken, messageId)
            .exchange(getRouter())
            .bodyToMono(Void.class);
    }
}
