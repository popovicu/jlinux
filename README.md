# jLinux!!

This is an exploration similar to [Ultimate Linux](https://github.com/popovicu/ultimate-linux/) where the userspace is written in JavaScript. Please check out that mini repo for context on how I started doing these explorations.

jLinux is yet another jab at the comments I have received on my views with Linux, Unix, AI, etc. This time, the whole system is built in **Java**.

At the same time, it's a fun exploration on finding the most "independent" deployment of a Java binary.

Also, **100% of this code is written by AI**, there is not a single human-written line in that file. Because that's how we deliver true business value!

## Build instructions

**This project as it is assumes you have `musl-gcc` installed at `/usr/local/musl/bin/musl-gcc`.**

You can get this by running `make install` on the `musl` codebase. It won't interfere with your host's libc and it will drop the `gcc` and `clang` wrappers (such as `musl-gcc`).

Next, we nee GraalVM, for which you can download pre-built binaries from their website.

For GraalVM to work, you need the `musl` compiler on your path but it also needs to follow the right naming convention. I ran something like this on my machine:

```bash
# Setup musl wrapper
mkdir -p /tmp/musl-wrapper
ln -s /usr/local/musl/bin/musl-gcc /tmp/musl-wrapper/x86_64-linux-musl-gcc
export PATH=/tmp/musl-wrapper:$PATH
```

Lastly, your `musl-gcc` should have `Zlib` available for static linking. Just download the `Zlib` tar file, built and install it on a path where your `musl-gcc` can see it. Something like this:

```bash
CC=/usr/local/musl/bin/musl-gcc ./configure --static --prefix=/usr/local/musl
```

Now you should be ready to build jLinux!

```bash
./graalvm-jdk-25.0.1+8.1/bin/javac Init.java
./graalvm-jdk-25.0.1+8.1/bin/native-image --static --libc=musl -march=compatibility -o init Init
```

Let's run it on a VM! First, let's build `initramfs`.

```
echo "init" | cpio -o -H newc > image.cpio
```

Now let's run the VM with the jLinux as the PID 1!

```
qemu-system-x86_64 -m 4G -kernel /tmp/linux/linux-6.17.12/arch/x86/boot/bzImage -initrd ./image.cpio -nographic --enable-kvm -smp 8 -append "console=ttyS0 rdinit=/init"
```

After a long blob of text from QEMU, you should get the shell prompt and you can play around a bit:

```
[    0.885241] x86/mm: Checked W+X mappings: passed, no W+X pages found.
[    0.886504] x86/mm: Checking user space page tables
[    0.925696] x86/mm: Checked W+X mappings: passed, no W+X pages found.
[    0.926813] Run /init as init process
🚀 [JLINUX INIT] Mounting essential filesystems...
✅ [JLINUX INIT] /proc mounted successfully
✅ [JLINUX INIT] /sys mounted successfully
     ██╗██╗     ██╗███╗   ██╗██╗   ██╗██╗  ██╗
     ██║██║     ██║████╗  ██║██║   ██║╚██╗██╔╝
     ██║██║     ██║██╔██╗ ██║██║   ██║ ╚███╔╝
██   ██║██║     ██║██║╚██╗██║██║   ██║ ██╔██╗
╚█████╔╝███████╗██║██║ ╚████║╚██████╔╝██╔╝ ██╗
 ╚════╝ ╚══════╝╚═╝╚═╝  ╚═══╝ ╚═════╝ ╚═╝  ╚═╝

    🚀 JLINUX - Java-Powered Linux Init System 🚀
    Now with 100% more JVM overhead!
    Because systemd wasn't enterprise enough

💼 Powered by: GraalVM Native Image + musl libc
🎯 Mission: Prove that anything can be PID 1
📊 Memory usage: More than you'd like
⚡ Boot time: Measured in business quarters

╔══════════════════════════════════════════════════════╗
║         JLINUX COMMAND REFERENCE v1.0                ║
╚══════════════════════════════════════════════════════╝

📁 FILE OPERATIONS:
  ls [path]              - List directory (now with Java overhead!)
  cd <path>              - Change directory (setter injection pattern)
  cat <file>             - Display file (buffered I/O factory)
  mkdir <path>           - Create directory (filesystem bean)
  pwd                    - Print working directory (getter method)

💾 SYSTEM OPERATIONS:
  mount <src> <dst> [fs] - Mount filesystem (native interop!)
  umount <path>          - Unmount filesystem

ℹ️  INFO & FUN:
  java / stats / info    - Show JVM runtime statistics
  uptime                 - Show system uptime
  meme                   - Generate enterprise meme
  why                    - Question your life choices
  about                  - Learn about Jlinux
  help                   - Show this help

🚪 EXIT:
  exit                   - Exit shell (if not PID 1)
  poweroff / shutdown    - Halt the system

[☕ java@jlinux]:/#
```
