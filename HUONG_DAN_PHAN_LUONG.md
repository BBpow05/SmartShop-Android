# SmartShop – Phần Lương: Giỏ hàng, Yêu thích và Đơn hàng

Project dùng Kotlin, ViewBinding, RecyclerView/CardView, Glide và dữ liệu sản phẩm sẵn có.
Giữ nguyên `applicationId = com.vanlang.smartshop` và các phần khác của nhóm.

## Các chức năng đã lập trình

1. Thêm sản phẩm vào giỏ ở trang chủ, danh sách yêu thích, chi tiết sản phẩm.
2. Tăng (+), giảm (−, về 0 tự xóa) và xóa (✕) sản phẩm; tính tổng tiền theo đơn giá × số lượng.
3. Thêm/bỏ yêu thích trên trang chủ, trang chi tiết; danh sách yêu thích có thể thêm hàng vào giỏ.
4. Đặt hàng: nhập tên, số điện thoại, địa chỉ, kiểm tra đầu vào và xác nhận.
5. Lịch sử đơn hàng: xem mã, thời gian, từng sản phẩm, số lượng, tổng tiền và người nhận.
6. Giữ giỏ hàng, danh sách yêu thích và lịch sử đơn hàng sau khi đóng và mở lại ứng dụng bằng SharedPreferences (JSON cục bộ).

**Giới hạn:** Đây là app demo học tập, **không xử lý thanh toán thật**, không đồng bộ đơn hàng với máy chủ/tài khoản khác, không hỗ trợ khuyến mãi/phí giao hàng. Đơn đã đặt có trạng thái demo "Đã đặt".

## Vị trí mã nguồn chính

- `repository/CartRepository.kt` – quản lý số lượng, tính tổng.
- `repository/FavoriteRepository.kt` – bật/tắt sản phẩm yêu thích.
- `repository/OrderRepository.kt` – tạo đơn, lịch sử đơn.
- `repository/ShopStorage.kt` – lưu dữ liệu qua SharedPreferences.
- `model/CustomerOrder.kt` – thông tin đơn và các dòng hàng.
- `ui/cart/CartActivity.kt` – giao diện giỏ hàng.
- `ui/cart/CheckoutActivity.kt` – thông tin giao hàng và xác nhận.
- `ui/order/OrderHistoryActivity.kt` – danh sách đơn hàng.
- `ui/favorite/FavoriteActivity.kt` – danh sách yêu thích.
- `ui/product/ProductDetailActivity.kt` – tích hợp các nút giỏ/yêu thích từ chi tiết.

## Cách chạy

1. Giải nén ZIP, Android Studio > Open > chọn thư mục `SmartShop-Android-main`.
2. Chờ Gradle Sync, chạy module `app` trên máy ảo Android.
3. Không cần cấu hình Firebase cho những chức năng này.

## Kiểm thử trên máy ảo (cần làm trước khi nộp)

- [ ] Tại Trang chủ, bấm **Thêm giỏ** cho sản phẩm A hai lần → giỏ hiển thị số lượng **2**.
- [ ] Bấm **+** → số lượng 3; bấm **−** → số lượng 2.
- [ ] Kiểm tra **Tổng cộng = đơn giá A × 2**.
- [ ] Bấm **✕** → sản phẩm A biến mất; giỏ hàng trống không cho đặt hàng.
- [ ] Bấm **♡** trên sản phẩm B → hiển thị trong **Yêu thích**; bấm **♥** tại đó → biến mất.
- [ ] Mở **Chi tiết sản phẩm**, bấm **Thêm vào giỏ hàng**, rồi bấm **Xem giỏ hàng**.
- [ ] Trong **Đặt hàng**, để trống các trường → có thông báo nhập liệu hợp lệ.
- [ ] Nhập đầy đủ, xác nhận đơn → giỏ hàng trống; lịch sử có đơn mới, có giá và số lượng đúng.
- [ ] Đóng hẳn ứng dụng và mở lại → dữ liệu yêu thích/đơn hàng vẫn còn.
- [ ] Trong **Tài khoản**, bấm **Xem lịch sử đơn hàng** → xem lại các đơn.

**Gợi ý ảnh minh chứng:** `01_gio_hang.png`, `02_yeu_thich.png`, `03_dat_hang.png`, `04_lich_su_don.png`.

## Phân chia công việc

Phần Lương phụ trách giỏ hàng, yêu thích, đặt hàng và lịch sử đơn. Những màn hình trang chủ, tìm kiếm, API sản phẩm, đăng nhập do các thành viên khác đảm nhận; cần kiểm tra tích hợp lại khi nhóm gộp code.
