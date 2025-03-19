package robotlib;

public class Camera {

    public Camera() {
    }

    public void setCameraAngle(int idx) {
        Data.userData[UserData.cameraIdx.ordinal()] = idx;
    }

    public void toggleCameraAngle() {
        int idx = Data.toInt(Data.userData[UserData.cameraIdx.ordinal()]);

        int cameraCount = 0;
        if (Game.robotIdx == 1)
            cameraCount = 3;
        else if (Game.robotIdx == 2)
            cameraCount = 4;
        idx = (idx + 1) % cameraCount;

        Data.userData[UserData.cameraIdx.ordinal()] = idx;
    }
}
