package com.wyq.rpc.core.remoting.netty.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @Author wyq
 * @Date 2022-06-23 20:17
 */
@Slf4j
public class HeartBeatHandler extends IdleStateHandler {

    private final long readerIdleTime;
    private final long writerIdleTime;

    public HeartBeatHandler(long readerIdleTime, long writerIdleTime, long allIdleTime, TimeUnit unit) {
        super(readerIdleTime, writerIdleTime, allIdleTime, unit);
        this.readerIdleTime = readerIdleTime;
        this.writerIdleTime = writerIdleTime;
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        IdleStateEvent event = (IdleStateEvent) evt;
        if (event.state() == IdleState.READER_IDLE) {
            log.info("[{}s] 没有收到读事件了", writerIdleTime);
        }
        super.userEventTriggered(ctx, evt);
    }
}
