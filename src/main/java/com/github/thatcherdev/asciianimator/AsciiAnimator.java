package com.github.thatcherdev.asciianimator;

import org.fusesource.jansi.Ansi;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AsciiAnimator {

	private static String file;
	private static boolean loop;
	private static int wait;
	final private static String help = "AsciiAnimator: A stop motion ASCII art animator (1.0.1)\n\nUsage:\n\tjava -jar asciianimator.jar [-h] [-v] [-f FILE -l BOOLEAN -fps INTEGER]\n\n"
			+ "Arguments:\n\t-h, --help\t\t\tDisplay this message.\n\t-v, --version\t\t\tDisplay current version.\n\t-f, --file\t\t\tSpecify file to use for animation. (See README.md for syntax)\n"
			+ "\t-l, --loop\t\t\tSpecify if the animation should loop. (Set to false by default)\n\t-fps, --frames-per-second\tSpecify FPS for animation. (Must be an integer greater than 0)\n\n"
			+ "Note: When running, CTRL + C can be used to terminate.";
	final private static Thread clear = new Thread() {
		public void run() {
			System.out.print(Ansi.ansi().eraseScreen().cursor(0, 0).reset());
		}
	};

	/**
	 * Starts AsciiAnimator based on command line arguments {@link args}.
	 * 
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		try {
			if (args.length == 0)
				throw new Exception();
			for (int k = 0; k < args.length; k++)
				if (args[k].equals("-h") || args[k].equals("--help")) {
					throw new Exception();
				} else if (args[k].equals("-v") || args[k].equals("--version")) {
					System.out.println(help.substring(0, help.indexOf("\n")));
					System.exit(0);
				} else if (args[k].equals("-f") || args[k].equals("--file"))
					file = args[++k];
				else if (args[k].equals("-l") || args[k].equals("--loop"))
					loop = Boolean.parseBoolean(args[++k]);
				else if (args[k].equals("-fps") || args[k].equals("--frames-per-second"))
					wait = 1000 / Integer.parseInt(args[++k]);
			if (file == null || wait == 0)
				throw new Exception();
			try {
				Runtime.getRuntime().addShutdownHook(clear);
				Animator animator = new Animator(file, loop, wait);
				clear.run();
				animator.start();
			} catch (Exception e) {
				Runtime.getRuntime().removeShutdownHook(clear);
				e.printStackTrace();
			}
		} catch (Exception e) {
			System.out.println(help);
			System.exit(0);
		}
	}
}