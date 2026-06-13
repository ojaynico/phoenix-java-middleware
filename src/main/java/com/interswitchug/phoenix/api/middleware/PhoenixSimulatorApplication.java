package com.interswitchug.phoenix.api.middleware;

import io.github.cdimascio.dotenv.Dotenv;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.security.Security;

@SpringBootApplication
public class PhoenixSimulatorApplication {

	public static void main(String[] args) {
		loadEnv();
		Security.addProvider(new BouncyCastleProvider());
		SpringApplication.run(PhoenixSimulatorApplication.class, args);
	}

	private static void loadEnv() {
		File envFile = findEnvFile();
		if (envFile == null) {
			System.out.println("⚠ .env not found, using system environment");
			return;
		}
		try {
			Dotenv dotenv = Dotenv.configure()
					.directory(envFile.getAbsoluteFile().getParent())
					.filename(envFile.getName())
					.ignoreIfMissing()
					.load();
			dotenv.entries().forEach(e -> System.setProperty(e.getKey(), e.getValue()));
			System.out.println("✓ .env loaded from: " + envFile.getAbsolutePath());
		} catch (Exception e) {
			System.out.println("✗ Failed to load .env: " + e.getMessage());
		}
	}

	private static File findEnvFile() {
		File current = new File(System.getProperty("user.dir"));
		for (int i = 0; i < 4; i++) {
			File candidate = new File(current, ".env");
			if (candidate.exists()) return candidate;
			File parent = current.getParentFile();
			if (parent == null) break;
			current = parent;
		}
		return null;
	}
}
