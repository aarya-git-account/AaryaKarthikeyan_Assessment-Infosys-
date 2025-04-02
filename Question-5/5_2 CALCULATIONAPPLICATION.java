import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class CalculatorApplication {

    private static AtomicInteger resultA = new AtomicInteger();
    private static AtomicInteger resultB = new AtomicInteger();
    
    private static ConcurrentHashMap<String, Boolean> taskStatus = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(4);
        
        try {
            System.out.println("Parallel tasks where A performs addition and B performs subtraction\n");
            Future<?> futureA = executor.submit(() -> {
                try {
                    int calculation = add(5, 3);
                    resultA.set(calculation);
                    taskStatus.put("A", true);
                    System.out.println("SUCCESS -> Task A: 5 + 3 = " + calculation);
                } 
                catch (Exception e) {
                    taskStatus.put("A", false);
                    System.out.println("FAILED -> Task A: " + e.getMessage());
                    throw e;
                }
            }); 
            
            Future<?> futureB = executor.submit(() -> {
                try {
                    int calculation = subtract(5, 3); 
                    resultB.set(calculation);
                    taskStatus.put("B", true);
                    System.out.println("SUCCESS -> Task B: 5 - 3 = " + calculation);
                } 
                catch (Exception e) {
                    taskStatus.put("B", false);
                    System.out.println("FAILED -> Task B: " + e.getMessage());
                    throw e;
                }
            });

            futureA.get();
            futureB.get();
            
            if (!taskStatus.getOrDefault("A", false) || !taskStatus.getOrDefault("B", false)) {
                throw new RuntimeException("Stage 1 failed -> cannot proceed");
            }

            System.out.println("\nParallel tasks where C performs multiplication and D performs division");
            Future<?> futureC = executor.submit(() -> {
                try {
                    int inputA = resultA.get();
                    int inputB = resultB.get();
                    int calculation = multiply(inputA, inputB);
                    taskStatus.put("C", true);
                    System.out.println("SUCCESS -> Task C: " + inputA + " × " + inputB + " = " + calculation);
                    return calculation;
                } 
                catch (Exception e) {
                    taskStatus.put("C", false);
                    System.out.println("FAILED -> Task C: " + e.getMessage());
                    throw e;
                }
            });
            
            Future<?> futureD = executor.submit(() -> {
                try {
                    int inputA = resultA.get();
                    int inputB = resultB.get();
                    int calculation = divide(inputA, inputB);
                    taskStatus.put("D", true);
                    System.out.println("SUCCESS -> Task D: " + inputA + " ÷ " + inputB + " = " + calculation);
                    return calculation;
                } 
                catch (Exception e) {
                    taskStatus.put("D", false);
                    System.out.println("FAILED -> Task D: " + e.getMessage());
                    throw e;
                }
            });

            System.out.println("\nIndependent task where E performs square of the input");
            Future<Integer> futureE = executor.submit(() -> {
                try {
                    int calculation = square(4); 
                    taskStatus.put("E", true);
                    System.out.println("SUCCESS -> Task E: 4^2 = " + calculation);
                    return calculation;
                } 
                catch (Exception e) {
                    taskStatus.put("E", false);
                    System.out.println("FAILED -> Task E: " + e.getMessage());
                    throw e;
                }
            });

            Integer resultC = (Integer) getWithTimeout(futureC, 2, TimeUnit.SECONDS, "C");
            Integer resultD = (Integer) getWithTimeout(futureD, 2, TimeUnit.SECONDS, "D");
            Integer resultE = getWithTimeout(futureE, 2, TimeUnit.SECONDS, "E");

            if (!taskStatus.getOrDefault("C", false) || !taskStatus.getOrDefault("D", false)) {
                throw new RuntimeException("Stage 2 failed - cannot proceed to final calculation");
            }
            
            System.out.println("\nIndependent Task E square result: " + resultE);

            System.out.println("\nFinal Task F that takes output of C and D as input");
            try {
                int finalResult = add(resultC, resultD);
                taskStatus.put("F", true);
                System.out.println("SUCCESS -> Task F: " + resultC + " + " + resultD + " = " + finalResult);
                System.out.println("\n=== FINAL RESULT ===");
                System.out.println("Main calculation result: " + finalResult);
                System.out.println("Independent square result: " + resultE);
            } 
            catch (Exception e) {
                taskStatus.put("F", false);
                System.out.println("FAILED -> Task F: " + e.getMessage());
            }

        } catch (Exception e) {
            System.err.println("System error: " + e.getMessage());
        } finally {
            executor.shutdown();
        }
    }

    private static int add(int a, int b) {
        return Math.addExact(a, b); 
    }
    
    private static int subtract(int a, int b) {
        return Math.subtractExact(a, b); 
    }
    
    private static int multiply(int a, int b) {
        return Math.multiplyExact(a, b); 
    }
    
    private static int divide(int a, int b) {
        if (b == 0) throw new ArithmeticException("Division by zero");
        return a / b;
    }
    
    private static int square(int a) {
        return multiply(a, a);
    }
    
    private static <T> T getWithTimeout(Future<T> future, long timeout, TimeUnit unit, String taskName) {
        try {
            return future.get(timeout, unit);
        } catch (TimeoutException e) {
            System.err.println("Task " + taskName + " timed out after " + timeout + " " + unit);
            taskStatus.put(taskName, false);  
            return null;  
        } catch (ExecutionException e) {
            System.err.println("FAILED -> Task " + taskName + ": " + e.getCause().getMessage());
            taskStatus.put(taskName, false);  
            return null;
        } catch (Exception e) {
            System.err.println("Unexpected error in Task " + taskName + ": " + e.getMessage());
            taskStatus.put(taskName, false);  
            return null;
        }
    }

}