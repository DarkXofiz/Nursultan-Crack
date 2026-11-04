# Termux + GitHub

Bu kaynak klasörünü Termux'a aldıktan sonra:

```sh
pkg update
pkg install openjdk-21 gradle git unzip zip
cd ~/Nursultan-OpenSource-1.21.1
java -version
mkdir -p ~/wrapper-tmp && cd ~/wrapper-tmp
gradle wrapper --gradle-version 9.2.1   # boş klasörde bir kez çalıştır
cp -r gradlew gradlew.bat gradle ~/Nursultan-OpenSource-1.21.1/
cd ~/Nursultan-OpenSource-1.21.1
```

Derle (ilk derleme Minecraft ve Fabric bağımlılıklarını indirir):

```sh
./gradlew build
```

Çıkan mod dosyası: `build/libs/nursultan-opensource-1.21.1-0.1.0.jar`
(`-sources.jar` ile biten dosyayı kullanma).

Telefonda derlemek yavaş veya yetersiz bellek yüzünden hata verirse, projeyi GitHub'a
yükle; `.github/workflows/build.yml` derlemeyi GitHub Actions üzerinde yapar ve JAR'ı
"Actions" sekmesinde ilgili çalıştırmanın "Artifacts" bölümünden indirebilirsin.

`./gradlew` Gradle 9.2.1 indirir; Termux paketindeki Gradle sürümü uyumsuz çıkarsa bu yol sorunu çözer.

Git deposu oluştur:

```sh
git config --global user.name "KULLANICI_ADI"
git config --global user.email "mail@ornek.com"
git init
git add .
git commit -m "Create clean Fabric 1.21.1 open-source starter"
git branch -M main
git remote add origin https://github.com/KULLANICI_ADI/Nursultan-OpenSource.git
git push -u origin main
```

`KULLANICI_ADI` ve depo adresini kendi GitHub hesabına göre değiştir.
GitHub artık `git push` için hesap şifresini kabul etmez; şifre sorulduğunda
GitHub'dan oluşturduğun bir Personal Access Token (PAT) kullan ve token'ı kimseyle paylaşma.
GitHub'a yüklemeden önce orijinal özel JAR'ı, hesap bilgilerini ve kişisel dosyaları ekleme.
