#!/usr/bin/env python3
"""Basic static indicator scan for a Java/Fabric JAR. Not a malware verdict."""
import sys, zipfile, re, hashlib
from pathlib import Path

if len(sys.argv) != 2:
    raise SystemExit("Usage: python audit_jar.py path/to/file.jar")

path = Path(sys.argv[1])
if not path.is_file():
    raise SystemExit(f"File not found: {path}")

flags = re.I
patterns = {
    "process execution": re.compile(rb"java/lang/ProcessBuilder|java/lang/Runtime(?![A-Za-z])", flags),
    "network APIs": re.compile(rb"java/net/Socket|java/net/HttpURLConnection|java/net/http/HttpClient", flags),
    "dynamic class loading": re.compile(rb"URLClassLoader|DexClassLoader|defineClass", flags),
    "credential-related strings": re.compile(rb"refresh_token|access_token|Login Data|Cookies|Local State", flags),
    "shell names": re.compile(rb"powershell|cmd\.exe|/bin/sh|bash -c", flags),
}
url_rx = re.compile(rb"https?://[^\x00-\x20\"<>\\]{4,250}", flags)

found = {label: [] for label in patterns}
urls = {}  # url -> set of class files

print("SHA256:", hashlib.sha256(path.read_bytes()).hexdigest())
with zipfile.ZipFile(path) as jar:
    entries = jar.namelist()
    classes = [n for n in entries if n.endswith(".class")]
    print("Entries:", len(entries))
    print("Class files:", len(classes))
    for name in classes:
        data = jar.read(name)
        for label, rx in patterns.items():
            if rx.search(data):
                found[label].append(name)
        for m in url_rx.finditer(data):
            urls.setdefault(m.group(0).decode("latin-1"), set()).add(name)

for label, names in found.items():
    print(f"\n{label}: {len(names)} class files")
    for name in names[:30]:
        print("  ", name)

print(f"\nremote URLs: {len(urls)} unique")
for url in sorted(urls)[:100]:
    print("  ", url, f"({len(urls[url])} class files)")

print("\nNOTE: Indicators can be legitimate library behavior. This script does not prove malware presence or absence.")
