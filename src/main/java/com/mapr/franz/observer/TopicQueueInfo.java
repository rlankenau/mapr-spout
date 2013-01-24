package com.mapr.franz.observer;

/**
 * This interface indicates the current state of a topic queue.  A queue may 
 * hold more than 2^64 messages, in which case only the 2^64 messages nearest 
 * the head of the queue will be available via the 
 * {@link com.mapr.franz.observer.TopicObserver} API. 
 * 
 * @author rlankenau
 *
 */
public interface TopicQueueInfo {
	
	/**
	 * Indicates whether the queue for this topic is empty.  If isEmpty() 
	 * returns false, the return value of {@link #getStart() getStart} and 
	 * {@link #getEnd() getEnd} should be ignored.
	 * 
	 * @return true if there are no queued messages for the topic, false 
	 * otherwise.
	 */
	public boolean isEmpty();
	
	/**
	 * Returns a sequence number that can be passed to 
	 * {@link com.mapr.franz.observer.TopicObserver#readMessage TopicObserver.readMessage} or 
	 * {@link com.mapr.franz.observer.TopicObserver#markMessage TopicObserver.markMessage}.
	 * 
	 * @return The sequence number for the next message to be dispatched from 
	 * the queue described by this object.  Undefined if 
	 * {@link #isEmpty() isEmpty} returns false.
	 */
	public long getStart();
	
	/**
	 * Returns a sequence number that can be passed to 
	 * {@link com.mapr.franz.observer.TopicObserver#readMessage TopicObserver.readMessage} or 
	 * {@link com.mapr.franz.observer.TopicObserver#markMessage TopicObserver.markMessage}.
	 *  
	 * @return The sequence number for the message most recently added to the 
	 * queue described by this object.  Undefined if {@link #isEmpty() isEmpty} 
	 * returns false.
	 */
	public long getEnd();
	
	/**
	 * Returns the topic name for the queue described by this object as a 
	 * string.  This topic string can be passed to any of the 
	 * {@link com.mapr.franz.observer.TopicObserver TopicObserver} methods that 
	 * require a topic.   
	 * 
	 * @return The topic for messages stored on the queue described by this 
	 * object.
	 */
	public String getTopic();
}
