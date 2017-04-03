package deadly.turtle;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    private static HashMap<String, long[]> stops = new HashMap<>();
    private static final int LEVELNO = 7;

    //ToDo Change file number, idiot!!!
    public static void main(String[] args) {
        try {
            for (int i = 1; i <= 4; i++) {
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("..\\..\\level" + LEVELNO + "\\level" + LEVELNO + "-" + i + ".txt"), "UTF-8"));
                String line = br.readLine();
                int count = Integer.parseInt(line);
                double t = 0;

                for (int k = 1; k <= count; k++) {
                    String[] data = br.readLine().split(" ");
                    stops.put(data[0], new long[]{Long.parseLong(data[1]), Long.parseLong(data[2])});
                }

                String[] targerJourney = br.readLine().split(" ");
                String hub = br.readLine();

                int countHyperLines = Integer.parseInt(br.readLine());
                List<String[]> hyperLines = new ArrayList<>();
                for (int j = 0; j < countHyperLines; j++) {
                    String[] asdf = br.readLine().split(" ");
                    hyperLines.add(Arrays.copyOfRange(asdf, 1, asdf.length));
                }
                List<String> locations = new ArrayList<>();
                for (String[] hyperLineRoute : hyperLines) {
                    for (String lineStop : hyperLineRoute) {
                        locations.add(lineStop);
                    }
                }
                String[] locationArray = new String[locations.size()];
                int closestStart = closestLocation(targerJourney[0], locations.toArray(locationArray));
                int closestEnd = closestLocation(targerJourney[1], locations.toArray(locationArray));

                String hyperStart = locations.get(closestStart);
                String hyperEnd = locations.get(closestEnd);

                int startLine = 0;
                int endLine = 0;
                for (int m = 0; m < hyperLines.size(); m++) {
                    if (Arrays.asList(hyperLines.get(m)).contains(hyperStart)) {
                        startLine = m;
                    }
                    if (Arrays.asList(hyperLines.get(m)).contains(hyperEnd)) {
                        endLine = m;
                    }
                }
                if(startLine == endLine) {
                    System.out.println(Math.round(journeyTime2(targerJourney, hyperLines.get(startLine))));
                } else {
                    t = 0;
                    String[] hypers = new String[]{targerJourney[0], hub};
                    t+= journeyTime2(hypers, hyperLines.get(startLine));
                    t+=300;
                    hypers = new String[]{hub, targerJourney[1]};
                    t+= journeyTime2(hypers, hyperLines.get(endLine));
                    System.out.println(Math.round(t));
                }
            }
//
//                System.out.println(greaterN(journeys, targetBenefical));


//                List<String[]> possHyper = stops.keySet().stream().map(entry -> stops.keySet().stream().filter(x -> !x.equals(entry)).map(x -> new String[]{x, entry})).collect(Collectors.toList());


//                System.out.println(counter);
//                String[] travel = br.readLine().split(" ");
//                long x1 = stops.get(travel[0])[0];
//                long y1 = stops.get(travel[0])[1];
//                long x2 = stops.get(travel[1])[0];
//                long y2 = stops.get(travel[1])[1];
//
//                double d = Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
//                t = d / 250 + 200;
//
//                System.out.println(Math.round(t));


//                    System.out.println(Arrays.toString(data));

//                        long x1 = Long.parseLong(data[1]);
//                        long y1 = Long.parseLong(data[2]);
//                        if ((line = br.readLine()) != null) {
//                            data = line.split(" ");
//                            if (data.length < 3 ) {
//                                break;
//                            }
//                            long x2 = Long.parseLong(data[1]);
//                            long y2 = Long.parseLong(data[2]);
//
//                            double d = Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
//                            t += d / 250 + 200;
//                        }
//1

//                System.out.println(Math.round(t));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static double travelDistance(String a, String b) {
        long x1 = stops.get(a)[0];
        long y1 = stops.get(a)[1];
        long x2 = stops.get(b)[0];
        long y2 = stops.get(b)[1];

        double d = Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));

        return d;
    }

    public static double travelTime(double distance, double speed) {
        return distance / speed;
    }

    public static double journeyTime(String[] journey, String[] hyper) {
        double tA = travelDistance(journey[0], hyper[0]);
        double tB = travelDistance(journey[0], hyper[1]);
        double t = 200.0;

        if (tA < tB) {
            t += travelTime(tA, 15.0);
            t += travelTime(travelDistance(hyper[1], journey[1]), 15.0);
        } else {
            t += travelTime(tB, 15.0);
            t += travelTime(travelDistance(hyper[0], journey[1]), 15.0);
        }
        t += travelTime(travelDistance(hyper[0], hyper[1]), 250.0);

        return t;
    }

    public static int beneficial2(List<String[]> journeys, String[] hyper) {
        int counter = 0;
        for (String[] journey : journeys) {
            double tH = journeyTime2(journey, hyper);
            if (tH < Double.parseDouble(journey[2])) {
                counter++;
            }
        }

        return counter;
    }

    public static int benefincal(List<String[]> journeys, String[] hyper) {
        int counter = 0;
        for (String[] journey : journeys) {
            double tH = journeyTime(journey, hyper);
            if (tH < Double.parseDouble(journey[2])) {
                counter++;
            }
        }

        return counter;
    }


    public static String greaterN(List<String[]> journeys, int targetBenefical) {
        List<String[]> possHyper = new ArrayList<>();
        for (String stop : stops.keySet()) {
            for (String stop2 : stops.keySet()) {
                if (stop.equals(stop2)) {
                    continue;
                } else {
                    String[] hyper = new String[]{stop, stop2};
                    if (benefincal(journeys, hyper) >= targetBenefical) {
                        return hyper[0] + " " + hyper[1];
                    }
                }
            }
        }
        throw new IllegalArgumentException();
    }

    public static int closestLocation(String startLoc, String[] locations) {
        HashMap<Integer, Double> distances = new HashMap<>();
        for (int i = 0; i < locations.length; i++) {
            distances.put(i, travelDistance(startLoc, locations[i]));
        }
        Comparator<Map.Entry<Integer, Double>> comparator = Comparator.comparing(Map.Entry::getValue);
        return distances.entrySet().stream().min(comparator).get().getKey();
    }

    public static double journeyTime2(String[] targetJourney, String[] hypers) {
        double t = 0.0;
        int closestStart = closestLocation(targetJourney[0], hypers);
        int closestEnd = closestLocation(targetJourney[1], hypers);

        if (closestStart > closestEnd) {
            for (int j = closestEnd; j < closestStart; j++) {
                t += 200;
                t += travelTime(travelDistance(hypers[j], hypers[j + 1]), 250.00);
            }
        } else {
            for (int j = closestStart; j < closestEnd; j++) {
                t += 200;
                t += travelTime(travelDistance(hypers[j], hypers[j + 1]), 250.00);
            }
        }

        t += travelTime(travelDistance(targetJourney[0], hypers[closestStart]), 15.00);
        t += travelTime(travelDistance(targetJourney[1], hypers[closestEnd]), 15.00);
        return Math.round(t);
    }

    public static double loopDistance(String[] hypers) {
        double d = 0.0;

        for (int j = 0; j < hypers.length - 1; j++) {
            d += travelDistance(hypers[j], hypers[j + 1]);
        }

        return d;
    }

    public static String[] randomLoop(int targetBenefical, int maxLength, List<String[]> journeys) {
        Random random = new Random();
        while (true) {
            int stopCounter = random.nextInt(stops.keySet().size() > 98 ? 98 : stops.keySet().size());
            if (stopCounter == 0) {
                stopCounter = 2;
            }
            List<String> keys = stops.keySet().stream().collect(Collectors.toList());
            Collections.shuffle(keys, random);
            String[] hypers = new String[stopCounter];
            for (int m = 0; m < stopCounter; m++) {
                hypers[m] = keys.get(m);
            }
            if (loopDistance(hypers) <= maxLength && beneficial2(journeys, hypers) >= targetBenefical) {
                return hypers;
            }
        }
    }
}
