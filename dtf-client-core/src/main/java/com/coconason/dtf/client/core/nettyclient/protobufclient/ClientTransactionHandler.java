package com.coconason.dtf.client.core.nettyclient.protobufclient;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.coconason.dtf.client.core.beans.TransactionServiceInfo;
import com.coconason.dtf.client.core.constants.DBOperationType;
import com.coconason.dtf.client.core.dbconnection.LockAndCondition;
import com.coconason.dtf.client.core.dbconnection.ThreadsInfo;
import com.coconason.dtf.common.constant.MessageType;
import com.coconason.dtf.common.protobuf.MessageProto;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
public class ClientTransactionHandler extends ChannelInboundHandlerAdapter
{

	@Autowired
	ThreadsInfo threadsInfo;

	private ChannelHandlerContext ctx;

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception
	{
		this.ctx = ctx;
		//sendMsg("90000","1","http://com.ping.test/pang","get","{id:90999}");
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
	{

		MessageProto.Message message = (MessageProto.Message) msg;
		JSONObject map = JSON.parseObject(message.getInfo());
		LockAndCondition lc = threadsInfo.get(map.get("threadId").toString());
		DBOperationType state = lc.getState();
		//1.If notified to be commit
		if(state == DBOperationType.COMMIT){
			lc.signal();
		}
		//2.If notified to be rollback
		else if(state == DBOperationType.ROLLBACK){
			lc.signal();
		}
		ctx.fireChannelRead(msg);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception
	{
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
	{
		ctx.fireExceptionCaught(cause);
	}

	public void sendMsg(TransactionServiceInfo serviceInfo) {
		sendMsg(serviceInfo.getGroupId(),serviceInfo.getGroupMemeberId(),serviceInfo.getMethod(),serviceInfo.getArgs());
	}

	public void sendMsg(String groupId, String groupMemeberId, Method method,Object[] args){
		MessageProto.Message.Builder builder= MessageProto.Message.newBuilder();
		builder.setType(MessageType.TRANSACTION_REQ);
		JSONObject info = new JSONObject();
		info.put("groupId",groupId);
		info.put("groupMemeberId",groupMemeberId);
		info.put("method",method);
		info.put("args",args);
		builder.setInfo(info.toJSONString());
		MessageProto.Message message = builder.build();
		System.out.println("Send transaction message:\n" + message);
		ctx.writeAndFlush(message);
	}
}
