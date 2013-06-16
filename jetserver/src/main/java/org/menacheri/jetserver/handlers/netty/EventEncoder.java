package org.menacheri.jetserver.handlers.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.MessageList;
import io.netty.handler.codec.MessageToMessageEncoder;

import org.menacheri.jetserver.event.Event;


/**
 * A simple event encoder will receive an incoming event, and convert it to a
 * {@link ByteBuf}. It will read the event type and put it as the
 * opcode(i.e first byte of the buffer), then it will read the event body and
 * put convert to ChannelBuffer if necessary and put it as the body of the
 * message.
 * 
 * @author Abraham Menacherry
 * 
 */
@Sharable
public class EventEncoder extends MessageToMessageEncoder<Event>
{
	
	@Override
	protected void encode(ChannelHandlerContext ctx, Event event,
			MessageList<Object> out) throws Exception
	{
		ByteBuf opcode = ctx.alloc().buffer(1);
		opcode.writeByte(event.getType());
		if(null != event.getSource())
		{
			ByteBuf data = (ByteBuf)event.getSource();
			ByteBuf compositeBuffer = Unpooled.wrappedBuffer(opcode,data);
			out.add(compositeBuffer);
		}
		else 
		{
			out.add(opcode);
		}
		
	}
	
}
