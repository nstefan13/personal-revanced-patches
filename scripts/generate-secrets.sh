#!/usr/bin/env bash

set -euo pipefail

output_file="${1:-SECRETS.txt}"

if [[ -n "${GPG_NAME:-}" ]]; then
    name_real="${GPG_NAME}"
else
    read -r -p "Name for the GPG key: " name_real
fi

if [[ -n "${GPG_EMAIL:-}" ]]; then
    name_email="${GPG_EMAIL}"
else
    read -r -p "Email for the GPG key: " name_email
fi

if [[ -n "${GPG_PASSPHRASE:-}" ]]; then
    passphrase="${GPG_PASSPHRASE}"
else
    read -r -s -p "Passphrase for the GPG key: " passphrase
    printf '\n'
fi

if [[ -z "${name_real}" || -z "${name_email}" || -z "${passphrase}" ]]; then
    printf 'Name, email, and passphrase are required.\n' >&2
    exit 1
fi

temp_gnupg_home="$(mktemp -d)"
temp_key_params="$(mktemp)"

cleanup() {
    rm -rf "${temp_gnupg_home}" "${temp_key_params}"
}

trap cleanup EXIT

cat >"${temp_key_params}" <<EOF
Key-Type: RSA
Key-Length: 4096
Subkey-Type: RSA
Subkey-Length: 4096
Name-Real: ${name_real}
Name-Email: ${name_email}
Expire-Date: 0
Passphrase: ${passphrase}
%commit
EOF

GNUPGHOME="${temp_gnupg_home}" gpg --batch --generate-key "${temp_key_params}" >/dev/null

fingerprint="$(GNUPGHOME="${temp_gnupg_home}" gpg --batch --with-colons --list-secret-keys "${name_email}" | awk -F: '/^fpr:/ { print $10; exit }')"

if [[ -z "${fingerprint}" ]]; then
    printf 'Failed to detect the GPG fingerprint.\n' >&2
    exit 1
fi

private_key="$(GNUPGHOME="${temp_gnupg_home}" gpg --armor --export-secret-keys "${fingerprint}")"

cat >"${output_file}" <<EOF
GPG_FINGERPRINT=${fingerprint}
GPG_PASSPHRASE=${passphrase}
GPG_PRIVATE_KEY<<EOF_PRIVATE_KEY
${private_key}
EOF_PRIVATE_KEY
EOF

chmod 600 "${output_file}"

printf 'Wrote %s\n' "${output_file}"