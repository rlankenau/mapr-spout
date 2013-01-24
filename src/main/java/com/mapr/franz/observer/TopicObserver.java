/**
 * 
 */
package com.mapr.franz.observer;

import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.util.List;


/**
 * The topic observer API consists of a library that allows listing of queues, 
 * reading of any queue from a defined point and querying of the current read 
 * point. The topic observer should deduplicate messages in the queue based on 
 * UUID, but is allowed to pass duplicate messages if they are not near enough 
 * to each other in the queue.
 * 
 * @author rlankenau
 *
 */
public interface TopicObserver {

	/**
	 * Returns the list of topic strings that can be passed to the 
	 * {@link #getQueueInfo(String) getQueueInfo}, {@link #readMessage readMessage}, 
	 * and {@link #markMessage markMessage} methods.
	 * 
	 * @return A list of topic strings.
	 */
	public List<String> getTopics();
	
	/**
	 * Returns a list of TopicQueueInfo objects describing the current state of
	 * all queues known to this TopicObserver.
	 * 
	 * @return A list of {@link com.mapr.franz.observer.TopicQueueInfo} objects.
	 */
	public List<TopicQueueInfo> getAllQueueInfo();

	/**
	 * Returns a TopicQueueInfo object describing the current state of a single 
	 * topic queue.
	 * 
	 * @return A {@link com.mapr.franz.observer.TopicQueueInfo} object, or null 
	 * if the topic is unknown.
	 */
	public TopicQueueInfo getQueueInfo(String topic);
	
	/**
	 * Fills an existing TopicQueueInfo object describing the current state of a
	 * single topic queue.
	 * 
	 * @return true if the topic is known, false otherwise.
	 */
	public boolean getQueueInfo(String topic, TopicQueueInfo info);

	/**
	 * Read a single message off of a queue.  Reading a message does not 
	 * indicate consumption.  
	 * 
	 * @param topic A topic string indicating the queue to be read.
	 * 
	 * @param sqn A sequence number specifying the message to be read.  This 
	 * should be between the start and end values returned by the 
	 * {@link #getQueueInfo} method.
	 *  
	 * @return A byte array containing the payload of the indicated message.
	 */
	public Byte[] readMessage(String topic, long sqn);
	
	/**
	 * Read a list of messages off of a queue.  Reading a message does not 
	 * indicate consumption.  
	 * 
	 * @param topic A topic string indicating the queue to be read.
	 * 
	 * @param start A sequence number specifying the first message to be read.  
	 * This should be between the start and end values returned by the 
	 * {@link #getQueueInfo} method, and less than the end parameter.
	 * 
	 * @param end A sequence number specifying the last message to be read.  
	 * This should be between the start and end values returned by the 
	 * {@link #getQueueInfo} method, and greater than the start parameter.
	 *   
	 * @return A list of byte arrays containing the payloads of the indicated 
	 * messages.
	 */
	public List<Byte[]> readMessage(String topic, long start, long end);
	
	/**
	 * Read a single message off of a queue, and write it to the supplied 
	 * ByteBuffer.  Reading a message does not indicate consumption.
	 * 
	 * @param topic A topic string indicating the queue to be read.
	 * 
	 * @param sqn A sequence number specifying the message to be read.  This 
	 * should be between the start and end values returned by the 
	 * {@link #getQueueInfo} method.
	 * 
	 * @param buff A ByteBuffer to be filled in with the message payload.  
	 * After readMessage returns, the position and limit are set to the start 
	 * and end of the data just written, respectively. 
	 */
	public void readMessage(String topic, long sqn, ByteBuffer buff) throws
		BufferOverflowException;
 
	
	/**
	 * Mark a message as consumed.  Implementations must advance the start value
	 * returned by getQueueInfo if the message with that sequence number is 
	 * marked as consumed.  If messages are consumed out of order, the behavior
	 * is undefined.
	 * 
	 * @param topic A topic string indicating the topic queue being updated.
	 * @param sqn The sequence number to mark as consumed.
	 */
	public void consumeMessage(String topic, long sqn);
	
	/**
	 * Mark a range of messages as consumed.  Implementations must advance the 
	 * start value returned by getQueueInfo if the message with that sequence 
	 * number is marked as consumed.  If messages are consumed out of order, the 
	 * behavior is undefined.
	 * 
	 * @param topic A topic string indicating the topic queue being updated.
	 * @param sqn The sequence number to mark as consumed.
	 */
	public void consumeMessage(String topic, long start, long end);
	
}
