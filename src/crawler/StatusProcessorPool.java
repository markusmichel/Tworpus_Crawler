/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package crawler;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Hashtable;
import java.util.Stack;
import twitter.StatusProcessor;

/**
 *
 * @author markus
 */
public class StatusProcessorPool {
    
    
    private Stack<Long> availableProcessors;
    private Hashtable<Long, StatusProcessor> processors;
    private int capacity;
    
    public StatusProcessorPool(int capacity) {
        availableProcessors = new Stack<>();
        processors = new Hashtable<>(capacity);
        this.capacity = capacity;
    }
    

    /**
     * Returns an available processors or creates one.
     * @return StatusProcessor
     * @throws Exception If all processors are working and if no more processors can be created.
     */
    public StatusProcessor get() throws Exception {
        try {
            // There are processors available -> return one
            Long id = availableProcessors.pop();
            return processors.get(id);
            
        } catch(EmptyStackException e) {
            // No processor available but capacity not reached
            // -> create new processor
            if(processors.size() < capacity) {
                long id = System.currentTimeMillis();
                StatusProcessor processor = new StatusProcessor(id, this);
                processors.put(id, processor);
                return processor;
            } else {
                // No processor available and maximum capacity reached
                throw new Exception("Cannot create more processors.");
            }
        }
    }
    
    public void setProcessAsleep(Long id) {
        availableProcessors.push(id);
    }
}
