# Nursultan-Fabric-1.21.11.jar — statik inceleme raporu

İncelenen dosya: `Nursultan-Fabric-1.21.11.jar`
SHA-256: `11c712383d0bda65840edb66f980a7074f989234e9d566959e6f8f5a9aa09a92`
Boyut: yaklaşık 56.4 MB
Arşiv girdisi: 48.476
`.class` dosyası: 26.403
`fabric.mod.json` girdisi: 1

## Doğrulanan bulgular

1. `fabric.mod.json`:
   - Mod ID: `nursultan`
   - Sürüm: `1.21.11`
   - Lisans: `Proprietary`
   - İki client entrypoint bildiriyor: `nursultan.NursultanModInit` ve `Nursultan.class09676`.
2. Ana JAR, Minecraft/Fabric ve çok sayıda üçüncü taraf bileşeni tek arşivde birleştirmiş görünüyor.
3. `Nursultan/` paketinde yaklaşık 2.999 sınıf var; bazı sınıf adları `classNNNNN` biçiminde ve kod karartma izlenimi veriyor.
4. `Main.class` başlangıcında Windows `cmd /c start` ile `https://discord.gg/mentcenter` adresini açmayı deneyen bir `ProcessBuilder` çağrısı bulundu. Hata yakalanıyor. Bu davranış RAT kanıtı değildir; ancak gereksiz ve platforma özel olduğundan temiz projeye alınmadı.
5. `Nursultan/class11324.class` içinde `https://nursultan.fun/config?id=` adresine giden uzak yapılandırma URL'si bulundu. İstek içeriği ve yanıtın kullanımı tam davranış analizi olmadan güvenli/zararlı diye sınıflandırılamaz.
6. Microsoft/Xbox/Minecraft kimlik doğrulama uç noktaları bulundu. Minecraft hesabı girişi için olağan olabilir; tokenların nerede saklandığı ve nasıl kullanıldığı ayrıca kod akışı izlenmeden kesinleştirilemez.
7. Arşivde imzalama dosyaları (`.SF`, `.RSA`, `.DSA`) bulunmadı.

## Sonuç

**RAT var/yok şeklinde kesin hüküm verilemez.** Bu çalışma statik ve sınırlı bir incelemedir; kod çalıştırılmadı, tüm kontrol akışları decompile edilip denetlenmedi ve dinamik ağ/dosya davranışı gözlenmedi. Özellikle uzak yapılandırma ve karartılmış ana istemci kodu nedeniyle orijinal JAR'ı güvenilir kabul etmeyin.

## Bu ZIP'in kapsamı

Bu ZIP, orijinal JAR'ın kaynak kodu değildir. Minecraft 1.21.1 için sıfırdan oluşturulmuş, Fabric mod desteği olan temiz bir başlangıç projesi ve bu inceleme raporunu içerir. Orijinal JAR ve içindeki üçüncü taraf kodlar yeniden dağıtılmamıştır.
