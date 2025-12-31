import org.graalvm.nativeimage.c.function.CFunction;
import org.graalvm.nativeimage.c.type.CCharPointer;
import org.graalvm.nativeimage.c.type.CTypeConversion;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Init {
    
    @CFunction
    private static native int mount(CCharPointer source, CCharPointer target, 
                                    CCharPointer filesystemtype, long mountflags, 
                                    long data);
    
    @CFunction
    private static native int umount(CCharPointer target);
    
    @CFunction
    private static native CCharPointer strerror(int errnum);
    
    static {
        try {
            new File("/proc").mkdir();
            new File("/sys").mkdir();
            new File("/dev").mkdir();
            new File("/tmp").mkdir();
        } catch (Exception e) {
        }
        
        mountEssentialFilesystems();
    }
    
    public static void main(String[] args) throws Exception {
        System.out.println("     ██╗██╗     ██╗███╗   ██╗██╗   ██╗██╗  ██╗");
        System.out.println("     ██║██║     ██║████╗  ██║██║   ██║╚██╗██╔╝");
        System.out.println("     ██║██║     ██║██╔██╗ ██║██║   ██║ ╚███╔╝ ");
        System.out.println("██   ██║██║     ██║██║╚██╗██║██║   ██║ ██╔██╗ ");
        System.out.println("╚█████╔╝███████╗██║██║ ╚████║╚██████╔╝██╔╝ ██╗");
        System.out.println(" ╚════╝ ╚══════╝╚═╝╚═╝  ╚═══╝ ╚═════╝ ╚═╝  ╚═╝");
        System.out.println();
        System.out.println("    🚀 JLINUX - Java-Powered Linux Init System 🚀");
        System.out.println("    Now with 100% more JVM overhead!");
        System.out.println("    Because systemd wasn't enterprise enough");
        System.out.println();
        System.out.println("💼 Powered by: GraalVM Native Image + musl libc");
        System.out.println("🎯 Mission: Prove that anything can be PID 1");
        System.out.println("📊 Memory usage: More than you'd like");
        System.out.println("⚡ Boot time: Measured in business quarters");
        System.out.println();
        
        printHelp();
        
        BufferedReader reader = new BufferedReader(
            new InputStreamReader(System.in)
        );
        
        int commandCount = 0;
        
        while (true) {
            String cwd = System.getProperty("user.dir", "/");
            
            // Rotating prompts for maximum enterprise vibes
            String[] prompts = {
                "☕ java@jlinux",
                "🏢 enterprise-grade",
                "💰 $$$ PROFIT $$$",
                "🔥 blazingly slow",
                "📈 synergizing",
                "🎩 fancy-pants",
                "🚨 PRODUCTION",
                "💎 premium-tier"
            };
            
            String prompt = prompts[commandCount % prompts.length];
            System.out.print("\033[1;36m[" + prompt + "]\033[0m:\033[1;33m" + cwd + "\033[0m# ");
            System.out.flush();
            
            String line = reader.readLine();
            if (line == null) {
                if (isPid1()) {
                    System.out.println("\n⚠️  Nice try! Can't exit when we're PID 1!");
                    System.out.println("💡 Tip: Use 'poweroff' to escape this Java nightmare");
                    continue;
                }
                break;
            }
            
            line = line.trim();
            if (line.isEmpty()) continue;
            
            commandCount++;
            
            String[] parts = line.split("\\s+");
            String cmd = parts[0];
            
            try {
                switch (cmd) {
                    case "ls":
                        ls(parts.length > 1 ? parts[1] : ".");
                        break;
                    
                    case "cd":
                        cd(parts.length > 1 ? parts[1] : "/");
                        break;
                    
                    case "cat":
                        if (parts.length < 2) {
                            System.out.println("usage: cat <file>");
                        } else {
                            cat(parts[1]);
                        }
                        break;
                    
                    case "mkdir":
                        if (parts.length < 2) {
                            System.out.println("usage: mkdir <path>");
                        } else {
                            mkdir(parts[1]);
                        }
                        break;
                    
                    case "pwd":
                        pwd();
                        break;
                    
                    case "mount":
                        if (parts.length < 3) {
                            System.out.println("usage: mount <source> <target> [fstype]");
                        } else {
                            String source = parts[1];
                            String target = parts[2];
                            String fstype = parts.length > 3 ? parts[3] : "auto";
                            doMount(source, target, fstype);
                        }
                        break;
                    
                    case "umount":
                        if (parts.length < 2) {
                            System.out.println("usage: umount <target>");
                        } else {
                            doUmount(parts[1]);
                        }
                        break;
                    
                    case "help":
                        printHelp();
                        break;
                    
                    case "java":
                    case "stats":
                    case "info":
                        javaInfo();
                        break;
                    
                    case "meme":
                        printMeme();
                        break;
                    
                    case "why":
                        printWhy();
                        break;
                    
                    case "about":
                        printAbout();
                        break;
                    
                    case "exit":
                        if (isPid1()) {
                            System.out.println("❌ ERROR: Cannot exit - running as PID 1");
                            System.out.println("🎭 You're stuck in the Java Matrix now!");
                            System.out.println("💡 Use 'poweroff' to escape");
                        } else {
                            System.out.println("👋 Goodbye from Jlinux! Thanks for the heap space!");
                            return;
                        }
                        break;
                    
                    case "poweroff":
                    case "shutdown":
                    case "halt":
                        System.out.println("🔌 Initiating Jlinux shutdown sequence...");
                        System.out.println("💾 Flushing enterprise beans...");
                        System.out.println("🗑️  Garbage collecting reality...");
                        System.out.println("👋 See you in the next reboot!");
                        Thread.sleep(500);
                        Runtime.getRuntime().halt(0);
                        break;
                    
                    case "uptime":
                        showUptime();
                        break;
                    
                    default:
                        System.out.println("❓ Unknown command: '" + cmd + "'");
                        System.out.println("💡 Type 'help' to see what Jlinux can do");
                        
                        // Easter eggs
                        if (cmd.equals("sl")) {
                            System.out.println("🚂 Did you mean 'ls'? Or are you looking for a train?");
                        } else if (cmd.equals("vim") || cmd.equals("emacs") || cmd.equals("nano")) {
                            System.out.println("😅 Sorry, no text editors here. This is Jlinux - we use IDEs!");
                        } else if (cmd.equals("sudo")) {
                            System.out.println("🎩 You're already root. We don't need sudo in Jlinux!");
                        } else if (cmd.equals("systemctl")) {
                            System.out.println("😏 systemctl? We ARE the init system now!");
                        }
                }
            } catch (Exception e) {
                System.err.println("💥 Exception in thread \"main\": " + e.getClass().getSimpleName());
                System.err.println("📍 Message: " + e.getMessage());
                System.err.println("🤔 Maybe try turning it off and on again?");
            }
        }
        
        if (isPid1()) {
            System.out.println("♾️  Init process entering eternal loop...");
            System.out.println("🎵 This is the loop that never ends...");
            while (true) {
                Thread.sleep(Long.MAX_VALUE);
            }
        }
    }
    
    static void printHelp() {
        System.out.println("╔══════════════════════════════════════════════════════╗");
        System.out.println("║         JLINUX COMMAND REFERENCE v1.0                ║");
        System.out.println("╚══════════════════════════════════════════════════════╝");
        System.out.println();
        System.out.println("📁 FILE OPERATIONS:");
        System.out.println("  ls [path]              - List directory (now with Java overhead!)");
        System.out.println("  cd <path>              - Change directory (setter injection pattern)");
        System.out.println("  cat <file>             - Display file (buffered I/O factory)");
        System.out.println("  mkdir <path>           - Create directory (filesystem bean)");
        System.out.println("  pwd                    - Print working directory (getter method)");
        System.out.println();
        System.out.println("💾 SYSTEM OPERATIONS:");
        System.out.println("  mount <src> <dst> [fs] - Mount filesystem (native interop!)");
        System.out.println("  umount <path>          - Unmount filesystem");
        System.out.println();
        System.out.println("ℹ️  INFO & FUN:");
        System.out.println("  java / stats / info    - Show JVM runtime statistics");
        System.out.println("  uptime                 - Show system uptime");
        System.out.println("  meme                   - Generate enterprise meme");
        System.out.println("  why                    - Question your life choices");
        System.out.println("  about                  - Learn about Jlinux");
        System.out.println("  help                   - Show this help");
        System.out.println();
        System.out.println("🚪 EXIT:");
        System.out.println("  exit                   - Exit shell (if not PID 1)");
        System.out.println("  poweroff / shutdown    - Halt the system");
        System.out.println();
    }
    
    static void ls(String path) throws IOException {
        File dir = new File(path);
        
        if (!dir.exists()) {
            System.out.println("❌ ls: cannot access '" + path + "': No such file or directory");
            return;
        }
        
        if (!dir.isDirectory()) {
            System.out.println("📄 " + path);
            return;
        }
        
        File[] files = dir.listFiles();
        if (files == null) {
            System.out.println("🔒 ls: cannot open directory '" + path + "': Permission denied");
            return;
        }
        
        Arrays.sort(files, Comparator.comparing(File::getName));
        
        for (File f : files) {
            String name = f.getName();
            if (f.isDirectory()) {
                System.out.print("\033[1;34m📁 " + name + "/\033[0m  ");
            } else {
                System.out.print("📄 " + name + "  ");
            }
        }
        System.out.println();
    }
    
    static void cd(String path) {
        File newDir = new File(path);
        
        if (!newDir.exists()) {
            System.out.println("❌ cd: " + path + ": No such file or directory");
            return;
        }
        
        if (!newDir.isDirectory()) {
            System.out.println("❌ cd: " + path + ": Not a directory");
            return;
        }
        
        try {
            System.setProperty("user.dir", newDir.getCanonicalPath());
            System.out.println("✅ Changed directory to: " + newDir.getCanonicalPath());
        } catch (IOException e) {
            System.out.println("❌ cd: " + e.getMessage());
        }
    }
    
    static void cat(String path) {
        try {
            Path filePath = Paths.get(path);
            List<String> lines = Files.readAllLines(filePath);
            for (String line : lines) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("❌ cat: " + path + ": " + e.getMessage());
        }
    }
    
    static void mkdir(String path) {
        File dir = new File(path);
        if (dir.mkdir()) {
            System.out.println("✅ Created directory: " + path);
        } else {
            System.out.println("❌ mkdir: cannot create directory '" + path + "'");
        }
    }
    
    static void pwd() {
        System.out.println("📍 " + System.getProperty("user.dir", "/"));
    }
    
    static void doMount(String source, String target, String fstype) {
        new File(target).mkdirs();
        
        System.out.println("🔧 Mounting " + source + " on " + target + " (type: " + fstype + ")...");
        
        try (CTypeConversion.CCharPointerHolder srcHolder = CTypeConversion.toCString(source);
             CTypeConversion.CCharPointerHolder tgtHolder = CTypeConversion.toCString(target);
             CTypeConversion.CCharPointerHolder fsHolder = CTypeConversion.toCString(fstype)) {
            
            int result = mount(srcHolder.get(), tgtHolder.get(), fsHolder.get(), 0, 0);
            
            if (result == 0) {
                System.out.println("✅ Successfully mounted " + source + " on " + target);
            } else {
                System.out.println("❌ mount failed with error code: " + result);
            }
        }
    }
    
    static void doUmount(String target) {
        System.out.println("🔧 Unmounting " + target + "...");
        
        try (CTypeConversion.CCharPointerHolder tgtHolder = CTypeConversion.toCString(target)) {
            int result = umount(tgtHolder.get());
            
            if (result == 0) {
                System.out.println("✅ Successfully unmounted " + target);
            } else {
                System.out.println("❌ umount failed with error code: " + result);
            }
        }
    }
    
    static void mountEssentialFilesystems() {
        System.out.println("🚀 [JLINUX INIT] Mounting essential filesystems...");
        
        try (CTypeConversion.CCharPointerHolder src1 = CTypeConversion.toCString("proc");
             CTypeConversion.CCharPointerHolder tgt1 = CTypeConversion.toCString("/proc");
             CTypeConversion.CCharPointerHolder fs1 = CTypeConversion.toCString("proc")) {
            
            int result = mount(src1.get(), tgt1.get(), fs1.get(), 0, 0);
            if (result == 0) {
                System.out.println("✅ [JLINUX INIT] /proc mounted successfully");
            } else {
                System.out.println("⚠️  [JLINUX INIT] /proc mount failed (code: " + result + ")");
            }
        }
        
        try (CTypeConversion.CCharPointerHolder src2 = CTypeConversion.toCString("sysfs");
             CTypeConversion.CCharPointerHolder tgt2 = CTypeConversion.toCString("/sys");
             CTypeConversion.CCharPointerHolder fs2 = CTypeConversion.toCString("sysfs")) {
            
            int result = mount(src2.get(), tgt2.get(), fs2.get(), 0, 0);
            if (result == 0) {
                System.out.println("✅ [JLINUX INIT] /sys mounted successfully");
            } else {
                System.out.println("⚠️  [JLINUX INIT] /sys mount failed (code: " + result + ")");
            }
        }
    }
    
    static void javaInfo() {
        System.out.println("╔══════════════════════════════════════════════════════╗");
        System.out.println("║          JLINUX RUNTIME TELEMETRY DASHBOARD          ║");
        System.out.println("╚══════════════════════════════════════════════════════╝");
        System.out.println();
        System.out.println("☕ Java Version:  " + System.getProperty("java.version"));
        System.out.println("🏢 Vendor:        " + System.getProperty("java.vendor"));
        System.out.println("⚙️  VM Name:       " + System.getProperty("java.vm.name"));
        System.out.println("🐧 OS:            " + System.getProperty("os.name") + " " + 
                          System.getProperty("os.version"));
        System.out.println("💻 Architecture:  " + System.getProperty("os.arch"));
        
        Runtime runtime = Runtime.getRuntime();
        long totalMem = runtime.totalMemory() / 1024 / 1024;
        long freeMem = runtime.freeMemory() / 1024 / 1024;
        long usedMem = totalMem - freeMem;
        
        System.out.println();
        System.out.println("💾 Memory (Total):     " + totalMem + " MB");
        System.out.println("📊 Memory (Used):      " + usedMem + " MB");
        System.out.println("🆓 Memory (Free):      " + freeMem + " MB");
        System.out.println("🔢 CPU Cores:          " + runtime.availableProcessors());
        System.out.println();
        System.out.println("🎯 PID 1 Status:       " + (isPid1() ? "YES - We ARE the init!" : "No"));
        System.out.println("🏷️  System Name:        Jlinux");
        System.out.println();
    }
    
    static void showUptime() {
        try {
            Path uptimePath = Paths.get("/proc/uptime");
            if (Files.exists(uptimePath)) {
                String uptimeStr = Files.readAllLines(uptimePath).get(0);
                String[] parts = uptimeStr.split("\\s+");
                double seconds = Double.parseDouble(parts[0]);
                
                long days = (long) (seconds / 86400);
                long hours = (long) ((seconds % 86400) / 3600);
                long minutes = (long) ((seconds % 3600) / 60);
                
                System.out.println("⏰ System uptime: " + days + " days, " + 
                                  hours + " hours, " + minutes + " minutes");
            } else {
                System.out.println("⚠️  Cannot read uptime (mount /proc first)");
            }
        } catch (Exception e) {
            System.out.println("❌ Error reading uptime: " + e.getMessage());
        }
    }
    
    static void printMeme() {
        String[] memes = {
            "\n" +
            "    \"What if we put a JVM in PID 1?\"\n" +
            "    \"That's the dumbest idea I've ever heard.\"\n" +
            "    \"Let's do it anyway.\"\n" +
            "    - The Jlinux project, probably\n",
            
            "\n" +
            "    Interviewer: Where do you see yourself in 5 years?\n" +
            "    Me: Running Java as my init process\n" +
            "    Interviewer: ...\n" +
            "    Me: You're hired!\n",
            
            "\n" +
            "    Enterprise Architect: We need maximum abstraction\n" +
            "    Developer: How much abstraction?\n" +
            "    Enterprise Architect: Yes.\n" +
            "    *Creates Jlinux*\n",
            
            "\n" +
            "    Friend: Why is your boot time so slow?\n" +
            "    Me: *sweating* Totally not because of Java\n" +
            "    Also Me: *GC pause intensifies*\n",
            
            "\n" +
            "    systemd: I'm bloated\n" +
            "    Jlinux: Hold my .jar file\n",
            
            "\n" +
            "    Nobody:\n" +
            "    Absolutely nobody:\n" +
            "    Enterprise developers: Let's make Linux but in Java\n"
        };
        
        Random rand = new Random();
        System.out.println(memes[rand.nextInt(memes.length)]);
    }
    
    static void printWhy() {
        System.out.println("\n🤔 Why did we build Jlinux?");
        System.out.println();
        System.out.println("   \"Science isn't about WHY - it's about WHY NOT!\"");
        System.out.println("   - Cave Johnson, probably");
        System.out.println();
        System.out.println("📊 Official Reasons:");
        System.out.println("   ✓ Because we could");
        System.out.println("   ✓ To prove a point (we're still figuring out what point)");
        System.out.println("   ✓ Maximum enterprise compliance");
        System.out.println("   ✓ Someone said it was impossible");
        System.out.println("   ✓ For the memes");
        System.out.println("   ✓ Java everywhere (literally EVERYWHERE now)");
        System.out.println();
        System.out.println("⚠️  Warning: Do NOT use in production");
        System.out.println("   (Unless you want to become a legend)");
        System.out.println();
    }
    
    static void printAbout() {
        System.out.println("\n╔══════════════════════════════════════════════════════╗");
        System.out.println("║                  ABOUT JLINUX v1.0                   ║");
        System.out.println("╚══════════════════════════════════════════════════════╝");
        System.out.println();
        System.out.println("🎯 What is Jlinux?");
        System.out.println("   A Java-based init system (PID 1) that runs directly");
        System.out.println("   on the Linux kernel. Because enterprise architecture");
        System.out.println("   knows no bounds.");
        System.out.println();
        System.out.println("🔧 Technology Stack:");
        System.out.println("   • GraalVM Native Image - AOT compilation");
        System.out.println("   • musl libc - Lightweight C library");
        System.out.println("   • Static linking - Zero dependencies");
        System.out.println("   • @CFunction interop - Direct syscalls");
        System.out.println();
        System.out.println("✨ Features:");
        System.out.println("   • File operations (ls, cd, cat, mkdir, pwd)");
        System.out.println("   • Filesystem mounting (mount, umount)");
        System.out.println("   • System info and statistics");
        System.out.println("   • Easter eggs and memes");
        System.out.println("   • Unmatched enterprise vibes");
        System.out.println();
        System.out.println("⚡ Performance:");
        System.out.println("   • Boot time: Measured in geological epochs");
        System.out.println("   • Memory usage: Yes");
        System.out.println("   • Speed: Blazingly slow™");
        System.out.println();
        System.out.println("💡 Created as a proof-of-concept that Java can do");
        System.out.println("   literally anything, including things it probably");
        System.out.println("   shouldn't.");
        System.out.println();
    }
    
    static boolean isPid1() {
        try {
            return new File("/proc/self").getCanonicalFile().getName().equals("1");
        } catch (Exception e) {
            return false;
        }
    }
}
