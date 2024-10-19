# Face Gallery App

## Overview
This Android app is designed to scan photos in the device's gallery, detect faces in each image using Google’s MediaPipe library, and allow users to tag and manage detected faces. The app follows the MVVM (Model-View-ViewModel) architecture and is built using Kotlin and Jetpack Compose for modern UI design. The app features three main screens: a Splash screen, a Main Gallery screen, and a Photo Detail view.

## Key Features
- **Splash Screen**: Displays the app logo and navigates to the Main Gallery screen.
- **Main Gallery Screen**:
  - Handles user permissions for gallery access.
  - Displays scanned images with detected faces in a grid view.
  - Shows scanning progress and completion status.
- **Photo Detail View**:
  - Displays the selected photo with detected faces marked using MediaPipe.
  - Allows users to add/update names of the detected faces using a bottom sheet.

## Libraries Used
- **Kotlin**: Programming language.
- **Jetpack Compose**: Modern UI toolkit for Android apps.
- **MediaPipe**: Face detection and tracking library from Google.

## Architecture
### Pattern Used: MVVM (Model-View-ViewModel)

### Files Structure
Files structure
FaceGallery/
├── SplashActivity.kt
├── MainActivity.kt
├── data/
│   ├── model/
│   │   ├── Faces.kt
│   │   └── Photo.kt
│   ├── repository/
│   │   └── GalleryRepository.kt
│   └── local/
│       ├── AppDatabase.kt
│       └── PhotoDao.kt
├── ui/
│   ├── gallery/
│   │   ├── compose/
│   │   │   ├── LottieAnimation.kt
│   │   │   ├── LottieSeekBarLoader.kt
│   │   │   └── RequestPermissionCompose.kt
│   │   ├── GalleryScreen.kt
│   │   ├── GalleryViewModel.kt
│   │   └── GalleryViewModelFactory.kt
│   ├── detail/
│   │   ├── PhotoDetailCompose.kt
│   │   ├── PhotoDetailFragment.kt
│   │   ├── PhotoDetailViewModel.kt
│   │   └── PhotoDetailViewModelFactory.kt
│   └── main/
│       ├── MainViewModel.kt
│       └── MainViewModelFactory.kt
├── helper/
│   ├── FaceDetectorHelper.kt
│   ├── FragmentNavigation.kt
│   └── MediaStoreObserver.kt
└── util/
    ├── OverlayView.kt
    ├── PermissionManager.kt
    └── PermissionPreferences.kt



## App Flow and Functionality

### Step-by-step Working of the App:

1. **App Open**:
   The app opens with the Splash screen.

2. **Splash Screen to Gallery**:
   After a brief display, the Splash screen automatically navigates to the Main Gallery page.

3. **Permission Handling**:
   The app checks for permission to access the device’s gallery. If permission is denied, it requests the user to grant access.

4. **Loading and Scanning Photos**:
   Once permission is granted, the app invokes the `loadPhotos()` method from the Gallery repository.
   - The method scans all photos on the device using a cursor to load images.
   - For each image:
     - If the photo is already in the database, it is ignored.
     - If the photo is not in the database, the app uses the MediaPipe library to detect faces.
     - The image (with or without faces) is saved to the database.
   - Scanned images are displayed in the Main Gallery screen via LiveData.

5. **Main Gallery Screen**:
   Displays all scanned images in a grid format and shows the current status of photo scanning (e.g., number of photos scanned and the completion status).

6. **Photo Detail View**:
   When the user clicks on an image, the app navigates to the Photo Detail view.
   - The selected photo is displayed, and detected faces are highlighted with square frames.
   - Each face can be tagged with a name. The user can click the "Add Names" button to add/update names using a bottom sheet UI.
   - The updated names are saved to the database.

7. **Handling App Relaunch**:
   Upon app relaunch, the app first displays all photos stored in the database.
   - It then rescans the device's gallery to detect any new or previously unscanned photos, updating the gallery in real time.

## Getting Started

### Prerequisites:
- Android Studio with Kotlin support.
- Set up the required permissions for accessing the gallery and handling media.

### Setup:
1. Clone the repository and open the project in Android Studio.
2. Make sure you have the required Android SDK and build tools installed.
3. Compile and run the project on an Android device or emulator.

### Permissions:
The app requires the following permissions in the `AndroidManifest.xml`:
- `READ_EXTERNAL_STORAGE` for accessing gallery images.
- `WRITE_EXTERNAL_STORAGE` for saving updated images (if necessary).

## How It Works

### Face Detection:
MediaPipe is used for real-time face detection. For each image, the library analyzes the photo to detect faces and marks them with bounding boxes. These bounding boxes are shown in the Photo Detail view, allowing users to tag and manage face data.

### Database:
A local database stores information about each scanned image, including the face detection results. This prevents redundant scanning of the same photos and allows quick app relaunches by displaying stored images first.

## Future Improvements:
- Better UI with placeholder ripple effect until images are loaded.
- Performance optimization for faster image processing.
- Handling cases when the image is deleted from the gallery.
- Add names when tapping on a face.
- Create Compose view utilities.
- FaceDetector configurations are hardcoded, add a screen to change these values.
