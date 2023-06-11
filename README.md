# auto-parts

Öncelikle Merhaba,

Dar zamanda geniş kapsamlı bir proje ile uğraştım Elimden geleni yaptım ancak eksikleri var. Swagger dökümanı ile başlayalım.

*User
![image](https://github.com/devakdogan/auto-parts/assets/106448958/bc9ade0b-93a5-4ded-bbe9-49d77bdcab06)

*Product
![image](https://github.com/devakdogan/auto-parts/assets/106448958/87bc039e-c5a4-4e92-b3c8-5f3d2584cf78)

*Order
![image](https://github.com/devakdogan/auto-parts/assets/106448958/1bbc8e98-825b-482a-a066-162d4380da1e)

*Jwt
![image](https://github.com/devakdogan/auto-parts/assets/106448958/c33a10bd-5628-47e6-93c4-2ac0b0b42838)

*Category
![image](https://github.com/devakdogan/auto-parts/assets/106448958/efce1a0a-7f45-4a03-91d1-b07a39e5b0ac)

*Shopping Cart
![image](https://github.com/devakdogan/auto-parts/assets/106448958/b13d4dcf-3b73-4ecf-84dd-863a9e308b22)

*Brand
![image](https://github.com/devakdogan/auto-parts/assets/106448958/ce8fd718-2b86-45e9-9bca-7e82c04e79f6)

*Address
![image](https://github.com/devakdogan/auto-parts/assets/106448958/47591372-9533-49a1-8390-1028fa18b526)

*Image
![image](https://github.com/devakdogan/auto-parts/assets/106448958/fcaae43f-d906-495c-a806-dcd9245afe3f)

*Contact Message - Database ve Credit Card
![image](https://github.com/devakdogan/auto-parts/assets/106448958/726f1f2d-046b-4c30-a463-3dadc36b482d)

Girişte bizi böyle bir sayfa karşılıyor
![image](https://github.com/devakdogan/auto-parts/assets/106448958/e8a20e6e-4a60-4f90-a229-2310a12314c6)

Admin olmayan kullanıcılar yalnızca kategorisi,brandi ve ürünün kendisi published olduysa öyle görebiliyor. Admin tüm ürünleri görebiliyor.
Ürün filtreleme tamamen dinamik. Birden fazla filtre ile ürün filtreleyerek çalışıyor.
![image](https://github.com/devakdogan/auto-parts/assets/106448958/5b524e22-1e8b-4d8a-a63a-ebc337fca7a7)
![image](https://github.com/devakdogan/auto-parts/assets/106448958/f8d5f1ec-af9f-4036-8902-e99670269acf)

Register olan kullanıcıya hesabı aktifleştirmesi için mail geliyor.
![image](https://github.com/devakdogan/auto-parts/assets/106448958/689dd556-a23c-445c-8ea8-3ff1231cb722)

Tıklandığında hesap aktif hale geliyor.
![image](https://github.com/devakdogan/auto-parts/assets/106448958/589d98e9-1b64-4398-bef8-d690ed2f27c6)
Ben daha önce aktif ettiğim için already confirmed aldım.

Contact us kısmından adminlere mail atılabiliyor. Eğer login olmuş bir kullanıcı varsa form kendiliğinden isim ve mail kısmını dolduruyor.
![image](https://github.com/devakdogan/auto-parts/assets/106448958/ebd00eb6-85c0-406c-b52e-e5d99552ff36)
login olmuş kullanıcı:
![image](https://github.com/devakdogan/auto-parts/assets/106448958/5bc8ead0-4fe4-43d6-9cdf-88825e3bbe33)

Giriş anında sepet get edilirken kullanıcıya bir cartUUID tanımlanıyor. Böylece kullanıcının anonym şekilde sepete eklediği ürünler kaybolmuyor. Login esnasında stock kontrolü ile birleşiyor.
![image](https://github.com/devakdogan/auto-parts/assets/106448958/86d7cbbe-d4f6-45ea-aad1-6a6709e50dd4)
![image](https://github.com/devakdogan/auto-parts/assets/106448958/c7e1866f-eabc-4ed3-9805-95c96088414c)
Aynı ürün login esnasında geldi:
![image](https://github.com/devakdogan/auto-parts/assets/106448958/3db8595c-1891-45d5-9e00-cd9898092312)
Admin panelden dilediği ürünü güncelliyor. Tüm verileri takip edebiliyor.
![image](https://github.com/devakdogan/auto-parts/assets/106448958/9adbcaa8-a676-44a4-9741-532fb0aa5774)

ürün ekleme ve detay kontrol etme
![image](https://github.com/devakdogan/auto-parts/assets/106448958/07044926-6a55-46de-a658-6283f37c1633)
![image](https://github.com/devakdogan/auto-parts/assets/106448958/e0e89733-0dfa-4bf0-92a8-2a49c15b9461)

Category ekleme veya düzenleme
![image](https://github.com/devakdogan/auto-parts/assets/106448958/4c32ca06-ca77-4e90-9a26-3e5cc5166a32)
![image](https://github.com/devakdogan/auto-parts/assets/106448958/e4f42715-eb14-4124-93e5-2bf96c8e7ba0)

Brand ekleme veya editleme
![image](https://github.com/devakdogan/auto-parts/assets/106448958/cf4cacb7-bed5-4a12-bba0-3a322ff0129d)
![image](https://github.com/devakdogan/auto-parts/assets/106448958/af73d3fa-309d-4edd-98ff-753fe6c9e676)

User kayıtları ve düzenleme
![image](https://github.com/devakdogan/auto-parts/assets/106448958/157ef0e5-d72a-4654-8705-acddf4f13813)
![image](https://github.com/devakdogan/auto-parts/assets/106448958/26b712e1-7e94-4591-9944-a02f6bf01e11)

Order kayıtları ve detay görme. Filtreler aktif çalışıyor.
![image](https://github.com/devakdogan/auto-parts/assets/106448958/baf3ba81-832b-49ff-bc79-b0ad1905715e)

Sepette ürün quantitysi güncellenebilir.
![image](https://github.com/devakdogan/auto-parts/assets/106448958/906aa606-1948-4f7f-91db-e28da42d86c2),
![image](https://github.com/devakdogan/auto-parts/assets/106448958/e62dbabe-2416-4d21-809d-6b9e885584ed)
 backend bağlantısı ile çalışıyor
 
 ödeme kısmında eğer kullanıcı save my payment info seçerse backend card bilgisini kaydediyor.
 ![image](https://github.com/devakdogan/auto-parts/assets/106448958/8dbf5c9e-00e8-493f-b15b-20b595fdf84f)
 gördüğünüz üzere kart bilgisi backende kaydolmuş
 ![image](https://github.com/devakdogan/auto-parts/assets/106448958/adf9ac7b-29a0-4470-9cdc-e651a1da3c0c)
 ![image](https://github.com/devakdogan/auto-parts/assets/106448958/90f5232d-da91-48de-86a6-03061efecde1)


 
 ödemeden sonra böyle bir form geliyor.
 
 ![image](https://github.com/devakdogan/auto-parts/assets/106448958/aa72322d-993f-4b0c-bef2-848b4a534ec5)
 
 
 Aynı zamanda bir de mail
 ![image](https://github.com/devakdogan/auto-parts/assets/106448958/d87f9f5c-306f-4a78-afda-d0a86acd725f)
 
 herhangi bir kullanıcı adminlere mail attığında da böyle bir mail geliyor 
 ![image](https://github.com/devakdogan/auto-parts/assets/106448958/6ed819a8-4ab3-4da1-b324-3c2745936585)



Biraz daha vaktim olsaydı daha güzel hale getirebilirdim ancak sağlık olsun. Bizlere bu fırsatı sunduğunuz için teşekkür ederim. Elimden geleni yaptım. Eksiklerim dil kısmı, Login esnasında account kilitleme ve cache kısımlarında oldu. Umarım beğenirsiniz :))



















