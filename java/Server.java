import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Server {
    private final static HashMap<String, Tree<?>> trees = new HashMap<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(4444)) {
            System.out.println("Server is listening on port 4444");

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected");

                new ServerThread(socket).start();
            }
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }


    private static class ServerThread extends Thread {
    private Socket socket;

    public ServerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
    
            String line;
            while (true) {
                line = in.readLine();

                String[] cmd = line.split(" ");
                if (cmd[0].equals("exit")) {
                    out.println("Exiting.");
                    System.out.println("Client disconected");
                    break;
                }
                
                try {
                    switch (cmd[0]) {
                        case "create" -> {
                            if (cmd.length < 3) { out.println("!! Invalid number of arguments"); return; }
                            if (cmd[1].length() == 0 ) { out.println("!! Must specity Tree name"); return; }
                            if (trees.containsKey(cmd[1])) { out.println("!! Tree with given name already exist"); return; }

                            Tree<?> tree;
                            switch (cmd[2]) {
                                case "int" -> { tree = new Tree<Integer>("int"); }
                                case "str" -> { tree = new Tree<String>("str"); }
                                case "dbl" -> { tree = new Tree<Double>("dbl"); }
                                default -> {
                                    out.println("!! Unknown tree type.");
                                    return;
                                }
                            }
                            trees.put(cmd[1], tree);

                            line = "Created tree '" + cmd[1] + "' of type " + cmd[2];
                            out.println(">> " + line);
                            System.out.println(line);
                        }

                        case "search" -> {
                            if (cmd.length < 3) { out.println("!! Invalid number of arguments"); return; }
                            if (cmd[1].length() == 0 ) { out.println("!! Must specity Tree name"); return; }
                            if (!trees.containsKey(cmd[1])) { out.println("!! Tree with given name does not exist"); return; }

                            boolean result;
                            switch (trees.get(cmd[1]).type) {
                                case "int" -> { result = ((Tree<Integer>) trees.get(cmd[1])).contains(Integer.valueOf(cmd[2])); }
                                case "str" -> { result = ((Tree<String>) trees.get(cmd[1])).contains(cmd[2]); }
                                case "dbl" -> { result = ((Tree<Double>) trees.get(cmd[1])).contains(Double.valueOf(cmd[2])); }
                                default -> {
                                    out.println("!! Unknown tree type");
                                    return;
                                }
                            }

                            out.println("'" + cmd[1] + (result ? "' contains " : "' does not contain ") + cmd[2]);
                        }

                        case "insert" -> {
                            if (cmd.length < 3) { out.println("!! Invalid number of arguments"); return; }
                            if (cmd[1].length() == 0 ) { out.println("!! Must specity Tree name"); return; }
                            if (!trees.containsKey(cmd[1])) { out.println("!! Tree with given name does not exist"); return; }

                            switch (trees.get(cmd[1]).type) {
                                case "int" -> { ((Tree<Integer>) trees.get(cmd[1])).insert(Integer.valueOf(cmd[2])); }
                                case "str" -> { ((Tree<String>) trees.get(cmd[1])).insert(cmd[2]); }
                                case "dbl" -> { ((Tree<Double>) trees.get(cmd[1])).insert(Double.valueOf(cmd[2])); }
                                default -> {
                                    out.println("!! Unknown tree type");
                                    return;
                                }
                            }

                            line = "Inserted " + cmd[2] + " into '" + cmd[1] + "'";
                            out.println(">> " + line);
                            System.out.println(line);
                        }

                        case "delete" -> {
                            if (cmd.length < 3) { out.println("!! Invalid number of arguments"); return; }
                            if (cmd[1].length() == 0 ) { out.println("!! Must specity Tree name"); return; }
                            if (!trees.containsKey(cmd[1])) { out.println("!! Tree with given name does not exist"); return; }

                            switch (trees.get(cmd[1]).type) {
                                case "int" -> { ((Tree<Integer>) trees.get(cmd[1])).delete(Integer.valueOf(cmd[2])); }
                                case "str" -> { ((Tree<String>) trees.get(cmd[1])).delete(cmd[2]); }
                                case "dbl" -> { ((Tree<Double>) trees.get(cmd[1])).delete(Double.valueOf(cmd[2])); }
                                default -> {
                                    out.println("!! Unknown tree type");
                                    return;
                                }
                            }

                            line = "Deleted " + cmd[2] + " from '" + cmd[1] + "'";
                            out.println(">> " + line);
                            System.out.println(line);
                        }

                        case "draw" -> {
                            if (cmd.length < 2) { out.println("!! Invalid number of arguments"); return; }
                            if (cmd[1].length() == 0 ) { out.println("!! Must specity Tree name"); return; }
                            if (!trees.containsKey(cmd[1])) { out.println("!! Tree with given name does not exist"); return; }
                            out.println(trees.get(cmd[1]).draw());
                        }

                        default -> { out.println("!! Unknown command"); }
                    }
                } catch (NumberFormatException ex) {
                    out.println("!! Bad argument");
                }

                out.println("test");
            }
    
            socket.close();
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
}
