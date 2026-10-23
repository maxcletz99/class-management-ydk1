# Class Management by SMK Yadika 1 — proyek APK

Proyek Android WebView yang dapat dibangun dari GitHub Actions, sehingga bisa diproses memakai HP Android tanpa memasang Android Studio.

## Konfigurasi website

URL website sudah diatur ke `https://classmanagementbyyadika.netlify.app/`. Nama aplikasi juga sudah diatur menjadi **Class Management by SMK Yadika 1**.

Jangan masukkan Supabase `service_role` key atau data pribadi siswa ke repository publik. Aplikasi ini menampilkan website HTTPS yang sudah ada; website dan Supabase tetap memerlukan internet.

## Cara build hanya dengan HP Android

1. Unduh ZIP proyek ini dan ekstrak dengan aplikasi pengelola file.
2. Buka `https://github.com` di Chrome, login atau buat akun.
3. Buat repository baru, misalnya `class-management-smk-yadika1` (Public memudahkan pemula; jangan masukkan data siswa atau rahasia ke repository).
4. Upload isi folder proyek ke repository, termasuk folder tersembunyi `.github/workflows`. Pastikan file `build-apk.yml` ada di `.github/workflows/`.
5. Buka tab **Actions** di repository. Pilih **Build Android APK**, lalu tekan **Run workflow**.
6. Tunggu sampai workflow berwarna hijau. Buka hasil run, gulir ke **Artifacts**, lalu unduh `Class-Management-SMK-Yadika-1-APK`.
7. Ekstrak ZIP artifact untuk mendapatkan `app-debug.apk`. Ketuk APK dan izinkan pemasangan dari sumber ini jika Android memintanya.

## Catatan

- URL website wajib diubah sebelum build. APK tidak akan membuka website sebenarnya selama masih menggunakan placeholder.
- APK debug cocok untuk pengujian dan dibagikan terbatas. Untuk Google Play atau rilis produksi, perlu konfigurasi signing key rilis yang dijaga aman.
- Website dan Supabase tetap membutuhkan internet. Perubahan pada website yang sama biasanya langsung terlihat di aplikasi tanpa build APK baru.
- Login dan upload file perlu diuji di perangkat. WebView memakai JavaScript dan DOM storage; pemilih file disediakan untuk input file HTML.
