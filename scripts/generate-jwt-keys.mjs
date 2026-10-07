import { generateKeyPairSync } from "node:crypto";
import { existsSync, mkdirSync, writeFileSync } from "node:fs";
import { fileURLToPath } from "node:url";
import { dirname, join } from "node:path";

const repositoryRoot = fileURLToPath(new URL("../", import.meta.url));
const secretsDirectory = join(repositoryRoot, "secrets");
const privateKeyPath = join(secretsDirectory, "jwt-private.pem");
const publicKeyPath = join(secretsDirectory, "jwt-public.pem");
const force = process.argv.includes("--force");

if (!force && (existsSync(privateKeyPath) || existsSync(publicKeyPath))) {
	throw new Error("JWT key files already exist. Use --force only to intentionally rotate them.");
}

mkdirSync(secretsDirectory, { recursive: true });

const { privateKey, publicKey } = generateKeyPairSync("rsa", {
	modulusLength: 3072,
	privateKeyEncoding: { type: "pkcs8", format: "pem" },
	publicKeyEncoding: { type: "spki", format: "pem" },
});

writeFileSync(privateKeyPath, privateKey, { mode: 0o600 });
writeFileSync(publicKeyPath, publicKey);
console.log(`Generated RSA JWT key pair in ${dirname(privateKeyPath)}.`);
