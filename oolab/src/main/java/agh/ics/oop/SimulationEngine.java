//package agh.ics.oop;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.TimeUnit;
//
//public class SimulationEngine {
//
//    private final List<Simulation> simulations;
//    private final ExecutorService executorService = Executors.newFixedThreadPool(4);
//    private final List<Thread> threads = new ArrayList<>();
//
//    public SimulationEngine(List<Simulation> simulations){
//        this.simulations = simulations;
//    }
//
//    public void runSync(){
//        for(var simulation : simulations){
//            simulation.run();
//        }
//    }
//
//    public void runAsync() {
//        for(var simulation : simulations){
//            var thread = new Thread(simulation);
//            thread.start();
//            threads.add(thread);
//        }
//        try {
//            awaitSimulationsEnd();
//        }catch (InterruptedException e){
//            System.out.println("Wątek został przerwany"+e.getMessage());
//        }
//    }
//
//    public void runAsyncInThreadPool(){
//        for(var simulation : simulations){
//            executorService.submit(simulation);
//        }
//        try {
//            awaitSimulationsEnd();
//        }
//        catch (InterruptedException e){
//            System.out.println("Wątek został przerwany"+e.getMessage());
//        }
//    }
//
//    public void awaitSimulationsEnd() throws InterruptedException {
//        // surowe wątki
//        for(var thread : threads){
//            thread.join();
//        }
//        //pula wątków
//        executorService.shutdown();
//        executorService.awaitTermination(10, TimeUnit.SECONDS);
//    }
//
//
//}
