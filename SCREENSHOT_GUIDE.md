# Screenshot guide for submission

Your app is working — these screenshots will let your professor see the
features in a logical order.

All screenshots go in:

```
D:\app_dev_proj\screenshots
```

---

## If the laboratory requirement is only the Login component

This is the recommended set to submit for a login-only task. Take these five
screenshots in order:

| # | File name | What to show | What it proves |
|---|---|---|---|
| 01 | `login-01-screen.png` | The login screen with Username, Password, Sign In button, and demo accounts | The login interface exists |
| 02 | `login-02-empty-fields.png` | Tap **Sign In** with both fields empty | Required-field validation works |
| 03 | `login-03-invalid.png` | Enter `admin` / `wrong`, then tap **Sign In** | Invalid credentials are rejected |
| 04 | `login-04-success.png` | Enter `admin` / `admin123`, then tap **Sign In** | Correct credentials log the user in — capture the dashboard header showing **SIGNED IN AS / Registrar / Administrator** |
| 05 | `login-05-logout.png` | Tap the logout icon, confirm, and capture the login screen again | Logout returns to the login screen |

Optional extra:

| # | File name | What to show | What it proves |
|---|---|---|---|
| 06 | `login-06-password-visible.png` | Tap the eye icon in the password field | Show/hide password works |

The rest of the app (student records, search, edit, delete) is beyond the
login requirement. You do **not** need to submit those screenshots unless your
professor asks for them.

### Step-by-step for each login screenshot

#### 01 — `login-01-screen.png`

1. Close the app completely: open the recent-apps screen and swipe **Student SIS** away.
2. Open **Student SIS** from the app drawer.
3. Wait for the login screen. Do not type anything.
4. Take the screenshot with **Power + Volume Down**.
5. The image should show the app title, Username field, Password field, the
   **Sign In** button, and the demo accounts card.

#### 02 — `login-02-empty-fields.png`

1. Stay on the fresh login screen.
2. Tap **Sign In** without entering a username or password.
3. Wait for the two red validation messages:
   - “Enter your username.”
   - “Enter your password.”
4. Take the screenshot.

#### 03 — `login-03-invalid.png`

1. Tap the **Username** field and type `admin`.
2. Tap the **Password** field and type `wrong`.
3. Tap **Sign In**.
4. Wait for the red message: “Incorrect username or password. Please try again.”
5. If the keyboard is covering the lower part of the screen, press **Back**
   once to hide it. Do not close the app.
6. Take the screenshot.

#### 04 — `login-04-success.png`

1. Tap the **Password** field and type `admin123`.
2. Tap **Sign In**.
3. Wait for the dashboard to open.
4. Make sure the top of the screen is visible. The header should show
   **SIGNED IN AS**, **Registrar**, and **Administrator**.
5. Take the screenshot. This is the proof that correct credentials log in.

#### 05 — `login-05-logout.png`

1. On the dashboard, tap the **logout icon** in the top-right corner
   (the door with an arrow).
2. A **Sign out?** dialog appears. Tap **Sign Out**.
3. The login screen reappears.
4. Take the screenshot.

#### Optional — `login-06-password-visible.png`

1. On the login screen, type `admin` and `admin123`.
2. Tap the **eye icon** on the right side of the Password field.
3. The password will be shown as plain text.
4. Press **Back** once to hide the keyboard, then take the screenshot.
5. Tap the eye icon again if you want to hide it.

### How to take and save each screenshot

On the phone, press **Power + Volume Down** at the same time. The screen will
flash, and the image is saved in **Gallery / Photos → Screenshots**.

Then transfer the files to the laptop:

1. Connect the phone using the USB-C cable.
2. On the phone, choose **File transfer (MTP)** from the USB notification.
3. On the laptop, open **File Explorer → This PC → [phone] → Internal storage**.
4. Open **DCIM/Screenshots** or **Pictures/Screenshots**.
5. Copy the images into `D:\app_dev_proj\screenshots`.
6. Rename them exactly as shown above.

If you enable USB debugging, you can skip the manual transfer and use:

```bat
capture-screenshot.bat login-01-screen
```

Run it once for each screen. The file is saved directly in the
`screenshots` folder.

### Login code locations, if she asks to see the implementation

- **Login screen layout:** `app\src\main\res\layout\activity_login.xml`
- **Login logic and validation:** `app\src\main\java\com\example\sis\LoginActivity.java`
- **Username/password checking:** `app\src\main\java\com\example\sis\data\AuthService.java`
- **Session hand-off after login:** dashboard launch in `LoginActivity.attemptLogin()`
  and the signed-in user header in `DashboardActivity`

If she wants code screenshots too, open those files in Android Studio and use
**Windows Snipping Tool** (`Win + Shift + S`) to capture the relevant methods.

---

## Full-app screenshot set (optional — only if requested)

1. **Clear the demo data for a consistent set of screenshots** (optional but
   recommended):
   **Settings → Apps → Student SIS → Storage → Clear storage**. On the next
   launch the eight sample students are restored.
2. Turn on **Do Not Disturb** so notification icons do not appear.
3. Set the phone brightness to a comfortable level and keep the phone in
   portrait orientation.
4. Log in with:
   - Username: `admin`
   - Password: `admin123`

---

## Required screenshot set

| # | File name | Screen | What it proves |
|---|---|---|---|
| 01 | `01-login.png` | Login screen | The app has a working login system |
| 02 | `02-dashboard.png` | Dashboard after login | Student records, statistics, and session state |
| 03 | `03-add-form.png` | Add Student form, filled in | Student number, name, program, and year-level inputs |
| 04 | `04-after-add.png` | Dashboard after saving | The new record and updated totals |
| 05 | `05-search.png` | Search results | Search/filter works |
| 06 | `06-edit-form.png` | Edit Student form, prefilled | Existing data is loaded for editing |
| 07 | `07-delete-dialog.png` | Delete confirmation dialog | Records can be removed safely |
| 08 | `08-logout.png` | Login screen after logout | Logout works |

Optional extra: `09-persistence.png` — close the app completely, reopen it,
log in again, and capture the dashboard to show that saved records survived.

---

## Suggested screenshot sequence

### 01 — Login

Open the app. Do not fill anything in. Capture the screen with the app title,
username/password fields, Sign In button, and demo accounts card.

### 02 — Dashboard

Log in as `admin` / `admin123`. Capture the dashboard with:

- the three statistics cards
- the eight seeded student records
- the search bar
- the Add Student button

### 03 — Add Student form

Tap **Add Student**. Fill in:

```
Student number: 2026-0009
Last name:      Abad
First name:     Carla
Middle name:    Mendoza
Program:        BS Information Technology
Year level:     2nd Year
```

Capture the screen **before** tapping the final **Add Student** button.

### 04 — After adding

Tap **Add Student**. Capture the dashboard with the new **Abad, Carla**
record visible and the total increased to **9**.

### 05 — Search

Type `Abad` in the search bar (or try `Nursing`). Capture the filtered result.

### 06 — Edit form

Clear the search, tap the **Abad** record, and capture the Edit Student screen
showing the prefilled values. Then change the year level to `3rd Year` and tap
**Save Changes**.

### 07 — Delete dialog

Open the three-dot menu on any record and choose **Delete**. Capture the
confirmation dialog. Tap **Cancel** if you want to keep all records for the
final screenshot.

### 08 — Logout

Tap the logout icon in the top-right corner and confirm. Capture the login
screen again.

---

## How to take the screenshots

### Manual method (works on every phone)

1. Press **Power + Volume Down** at the same time.
2. The screenshot is saved in **Gallery / Photos → Screenshots**.
3. Connect the phone to the laptop in **File transfer** mode and copy the
   images into `D:\app_dev_proj\screenshots`.
4. Rename them using the file names in the table above.

Some phones use different gestures:
- Samsung: **Power + Volume Down**
- Pixel: **Power + Volume Down**
- Xiaomi/Redmi/POCO: **Power + Volume Down**, or three-finger swipe down
- Oppo/Realme/Vivo: **Power + Volume Down**, or three-finger swipe down

### USB debugging method (one click per screenshot)

If USB debugging is enabled:

1. Connect the phone and accept the **Allow USB debugging** prompt.
2. Navigate to the screen you want.
3. Double-click `capture-screenshot.bat` and type a name when asked, or run:

```bat
capture-screenshot.bat 01-login
```

The image is saved automatically as `screenshots\01-login.png`.

Repeat for each screen:

```bat
capture-screenshot.bat 02-dashboard
capture-screenshot.bat 03-add-form
capture-screenshot.bat 04-after-add
capture-screenshot.bat 05-search
capture-screenshot.bat 06-edit-form
capture-screenshot.bat 07-delete-dialog
capture-screenshot.bat 08-logout
```

---

## If a screenshot shows the wrong data

Use **Clear storage** to restore the eight sample students, then repeat the
sequence. Do not include real personal information in your submission unless
your professor asked for it — the seeded sample records are enough.
