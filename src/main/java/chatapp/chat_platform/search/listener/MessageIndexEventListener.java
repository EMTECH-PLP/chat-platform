package chatapp.chat_platform.search.listener;

import chatapp.chat_platform.common.event.MessageCreatedEvent;
import chatapp.chat_platform.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

// Catches the announcement from chat and indexes the message.
// @TransactionalEventListener = only runs after chat's save actually commits.
// @Async = runs on a background thread, doesn't slow down sending a message.
@Component
@RequiredArgsConstructor
public class MessageIndexEventListener {

    private final SearchService searchService;

    @Async
    @TransactionalEventListener
    public void onMessageCreated(MessageCreatedEvent event) {
        searchService.index(
                event.messageId(),
                event.roomId(),
                event.senderId(),
                event.content()
        );
    }
}