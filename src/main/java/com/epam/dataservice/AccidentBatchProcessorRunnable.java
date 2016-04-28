package com.epam.dataservice;

import com.epam.data.RoadAccident;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Nick on 27.04.2016.
 */
public class AccidentBatchProcessorRunnable implements Runnable{

    BlockingQueue<List<RoadAccident>> dataQueue;
    BlockingQueue<List<RoadAccident>> resultQueue1;
    BlockingQueue<List<RoadAccident>> resultQueue2;

    public AccidentBatchProcessorRunnable(BlockingQueue<List<RoadAccident>> dataQueue, BlockingQueue<List<RoadAccident>> resultQueue1, BlockingQueue<List<RoadAccident>> resultQueue2) {
        this.dataQueue = dataQueue;
        this.resultQueue1 = resultQueue1;
        this.resultQueue2 = resultQueue2;
    }

    @Override
    public void run() {
        while(true) {
            try {

                List<RoadAccident> currentBatch = dataQueue.poll();

                List<RoadAccident> morningBatch = new ArrayList<>();
                List<RoadAccident> eveningBatch = new ArrayList<>();

                //Stupid processing here
                for (RoadAccident current: currentBatch) {
                    String no = current.getPoliceForce();
                    current.setDayTime(DayTime.getDayTime(current.getTime()));
                    //
                    if (current.getDayTime() == DayTime.MORNING || current.getDayTime() == DayTime.AFTERNOON) {
                        morningBatch.add(current);
                    } else {
                        if (current.getDayTime() == DayTime.EVENING || current.getDayTime() == DayTime.NIGHT) {
                            eveningBatch.add(current);
                        }

                    }
                }
                 if (morningBatch.size() > 0) {
                     System.out.println("Added " + morningBatch.size() + " to morning acc");
                     resultQueue1.add(morningBatch);
                 }
                 if (eveningBatch.size() > 0) {
                     System.out.println("Added " + eveningBatch.size() + " to evening acc");
                     resultQueue2.add(eveningBatch);
                 }


            } catch(Exception e){

            }



        }
    }
}