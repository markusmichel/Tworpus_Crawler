package crawler;

import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import twitter.StatusProcessor;

/**
 *
 * @author markus
 */
public class StatusProcessorPool {
    
    
    private PriorityQueue<StatusProcessor> processors;
    private final int capacity;
    private long idCounter;
    
    public StatusProcessorPool(int capacity) {
        StatusProcessorComparator comparator = new StatusProcessorComparator();
        processors = new PriorityQueue<StatusProcessor>(capacity, comparator);
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
            return processors.remove();
            
        } catch(NoSuchElementException e) {
            // No processor available but capacity not reached
            // -> create new processor
            if(processors.size() < capacity) {
                long id = idCounter++;
                StatusProcessor processor = new StatusProcessor(id, this);
                //processors.add(processor);
                return processor;
            } else {
                // No processor available and maximum capacity reached
                throw new Exception("Cannot create more processors.");
            }
        }
    }
    
    public void setProcessAsleep(StatusProcessor processor) {
        processors.add(processor);
    }
    
    class StatusProcessorComparator implements Comparator<StatusProcessor> {

        @Override
        public int compare(StatusProcessor p1, StatusProcessor p2) {
            if(p1.getCount() < p2.getCount()) return -1;
            else if(p1.getCount() > p2.getCount()) return 1;
            return 0;
        }
        
    }
}
