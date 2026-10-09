package com.senai.lajoju.security;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermission;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.Base64;
import java.util.EnumSet;
import java.util.Set;

public class GeradorChavesJwt {

	private static final int TAMANHO_CHAVE = 3072;

	public static void main(String[] args) throws IOException, GeneralSecurityException {
		boolean forcar = args.length == 1 && "--force".equals(args[0]);
		if (args.length > 0 && !forcar) {
			throw new IllegalArgumentException(
					"Uso: java usuario/src/main/java/com/senai/lajoju/security/GeradorChavesJwt.java [--force]");
		}

		Path diretorioSegredos = Path.of("secrets").toAbsolutePath();
		Path chavePrivada = diretorioSegredos.resolve("jwt-private.pem");
		Path chavePublica = diretorioSegredos.resolve("jwt-public.pem");

		if (!forcar && (Files.exists(chavePrivada) || Files.exists(chavePublica))) {
			throw new IllegalStateException(
					"As chaves JWT já existem. Use --force apenas para fazer uma rotação intencional.");
		}

		Files.createDirectories(diretorioSegredos);
		KeyPairGenerator gerador = KeyPairGenerator.getInstance("RSA");
		gerador.initialize(TAMANHO_CHAVE);
		KeyPair parChaves = gerador.generateKeyPair();

		escreverChave(chavePrivada, "PRIVATE KEY", parChaves.getPrivate().getEncoded(), forcar, true);
		escreverChave(chavePublica, "PUBLIC KEY", parChaves.getPublic().getEncoded(), forcar, false);
		System.out.println("Par de chaves RSA JWT criado em " + diretorioSegredos + ".");
	}

	private static void escreverChave(
			Path destino, String tipo, byte[] chave, boolean forcar, boolean privada) throws IOException {
		String conteudo = "-----BEGIN " + tipo + "-----\n"
				+ Base64.getMimeEncoder(64, new byte[] { '\n' }).encodeToString(chave)
				+ "\n-----END " + tipo + "-----\n";
		Path temporario = Files.createTempFile(destino.getParent(), ".jwt-", ".tmp");
		try {
			if (privada) {
				definirPermissoesPrivadas(temporario);
			}
			Files.writeString(temporario, conteudo);
			if (forcar) {
				Files.move(temporario, destino, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
			} else {
				Files.move(temporario, destino);
			}
		} finally {
			Files.deleteIfExists(temporario);
		}
	}

	private static void definirPermissoesPrivadas(Path arquivo) throws IOException {
		Set<PosixFilePermission> permissoes = EnumSet.of(
				PosixFilePermission.OWNER_READ,
				PosixFilePermission.OWNER_WRITE);
		try {
			Files.setPosixFilePermissions(arquivo, permissoes);
		} catch (UnsupportedOperationException ignored) {
			// Windows usa as permissões herdadas do diretório.
		}
	}
}
