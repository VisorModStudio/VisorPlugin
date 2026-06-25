package org.vmstudio.visor.protocol;

public enum DirectionValue {
    DOWN(0),
    UP(1),
    NORTH(2),
    SOUTH(3),
    WEST(4),
    EAST(5);

    private final int data3d;

    DirectionValue(int data3d){
        this.data3d = data3d;
    }

    public int get3DDataValue(){
        return data3d;
    }

    private static final DirectionValue[] BY_3D = new DirectionValue[6];

    static {
        for(DirectionValue d : values()){
            BY_3D[d.data3d] = d;
        }
    }

    public static DirectionValue from3DDataValue(int value){
        return BY_3D[Math.floorMod(value, BY_3D.length)];
    }
}
