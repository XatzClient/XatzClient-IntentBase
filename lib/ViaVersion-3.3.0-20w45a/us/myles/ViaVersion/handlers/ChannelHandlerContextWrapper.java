// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.handlers;

import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import io.netty.channel.ChannelProgressivePromise;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelPipeline;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelPromise;
import io.netty.channel.ChannelFuture;
import java.net.SocketAddress;
import io.netty.channel.ChannelHandler;
import io.netty.util.concurrent.EventExecutor;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

public class ChannelHandlerContextWrapper implements ChannelHandlerContext
{
    private final ChannelHandlerContext base;
    private final ViaHandler handler;
    
    public ChannelHandlerContextWrapper(final ChannelHandlerContext base, final ViaHandler handler) {
        this.base = base;
        this.handler = handler;
    }
    
    public Channel channel() {
        return this.base.channel();
    }
    
    public EventExecutor executor() {
        return this.base.executor();
    }
    
    public String name() {
        return this.base.name();
    }
    
    public ChannelHandler handler() {
        return this.base.handler();
    }
    
    public boolean isRemoved() {
        return this.base.isRemoved();
    }
    
    public ChannelHandlerContext fireChannelRegistered() {
        this.base.fireChannelRegistered();
        return (ChannelHandlerContext)this;
    }
    
    public ChannelHandlerContext fireChannelUnregistered() {
        this.base.fireChannelUnregistered();
        return (ChannelHandlerContext)this;
    }
    
    public ChannelHandlerContext fireChannelActive() {
        this.base.fireChannelActive();
        return (ChannelHandlerContext)this;
    }
    
    public ChannelHandlerContext fireChannelInactive() {
        this.base.fireChannelInactive();
        return (ChannelHandlerContext)this;
    }
    
    public ChannelHandlerContext fireExceptionCaught(final Throwable throwable) {
        this.base.fireExceptionCaught(throwable);
        return (ChannelHandlerContext)this;
    }
    
    public ChannelHandlerContext fireUserEventTriggered(final Object o) {
        this.base.fireUserEventTriggered(o);
        return (ChannelHandlerContext)this;
    }
    
    public ChannelHandlerContext fireChannelRead(final Object o) {
        this.base.fireChannelRead(o);
        return (ChannelHandlerContext)this;
    }
    
    public ChannelHandlerContext fireChannelReadComplete() {
        this.base.fireChannelReadComplete();
        return (ChannelHandlerContext)this;
    }
    
    public ChannelHandlerContext fireChannelWritabilityChanged() {
        this.base.fireChannelWritabilityChanged();
        return (ChannelHandlerContext)this;
    }
    
    public ChannelFuture bind(final SocketAddress socketAddress) {
        return this.base.bind(socketAddress);
    }
    
    public ChannelFuture connect(final SocketAddress socketAddress) {
        return this.base.connect(socketAddress);
    }
    
    public ChannelFuture connect(final SocketAddress socketAddress, final SocketAddress socketAddress1) {
        return this.base.connect(socketAddress, socketAddress1);
    }
    
    public ChannelFuture disconnect() {
        return this.base.disconnect();
    }
    
    public ChannelFuture close() {
        return this.base.close();
    }
    
    public ChannelFuture deregister() {
        return this.base.deregister();
    }
    
    public ChannelFuture bind(final SocketAddress socketAddress, final ChannelPromise channelPromise) {
        return this.base.bind(socketAddress, channelPromise);
    }
    
    public ChannelFuture connect(final SocketAddress socketAddress, final ChannelPromise channelPromise) {
        return this.base.connect(socketAddress, channelPromise);
    }
    
    public ChannelFuture connect(final SocketAddress socketAddress, final SocketAddress socketAddress1, final ChannelPromise channelPromise) {
        return this.base.connect(socketAddress, socketAddress1, channelPromise);
    }
    
    public ChannelFuture disconnect(final ChannelPromise channelPromise) {
        return this.base.disconnect(channelPromise);
    }
    
    public ChannelFuture close(final ChannelPromise channelPromise) {
        return this.base.close(channelPromise);
    }
    
    public ChannelFuture deregister(final ChannelPromise channelPromise) {
        return this.base.deregister(channelPromise);
    }
    
    public ChannelHandlerContext read() {
        this.base.read();
        return (ChannelHandlerContext)this;
    }
    
    public ChannelFuture write(final Object o) {
        if (o instanceof ByteBuf && this.transform((ByteBuf)o)) {
            return this.base.newFailedFuture(new Throwable());
        }
        return this.base.write(o);
    }
    
    public ChannelFuture write(final Object o, final ChannelPromise channelPromise) {
        if (o instanceof ByteBuf && this.transform((ByteBuf)o)) {
            return this.base.newFailedFuture(new Throwable());
        }
        return this.base.write(o, channelPromise);
    }
    
    public boolean transform(final ByteBuf buf) {
        try {
            this.handler.transform(buf);
            return false;
        }
        catch (Exception e) {
            try {
                this.handler.exceptionCaught(this.base, e);
            }
            catch (Exception e2) {
                this.base.fireExceptionCaught((Throwable)e2);
            }
            return true;
        }
    }
    
    public ChannelHandlerContext flush() {
        this.base.flush();
        return (ChannelHandlerContext)this;
    }
    
    public ChannelFuture writeAndFlush(final Object o, final ChannelPromise channelPromise) {
        final ChannelFuture future = this.write(o, channelPromise);
        this.flush();
        return future;
    }
    
    public ChannelFuture writeAndFlush(final Object o) {
        final ChannelFuture future = this.write(o);
        this.flush();
        return future;
    }
    
    public ChannelPipeline pipeline() {
        return this.base.pipeline();
    }
    
    public ByteBufAllocator alloc() {
        return this.base.alloc();
    }
    
    public ChannelPromise newPromise() {
        return this.base.newPromise();
    }
    
    public ChannelProgressivePromise newProgressivePromise() {
        return this.base.newProgressivePromise();
    }
    
    public ChannelFuture newSucceededFuture() {
        return this.base.newSucceededFuture();
    }
    
    public ChannelFuture newFailedFuture(final Throwable throwable) {
        return this.base.newFailedFuture(throwable);
    }
    
    public ChannelPromise voidPromise() {
        return this.base.voidPromise();
    }
    
    public <T> Attribute<T> attr(final AttributeKey<T> attributeKey) {
        return (Attribute<T>)this.base.attr((AttributeKey)attributeKey);
    }
}
