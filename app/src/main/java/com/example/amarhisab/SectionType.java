package com.example.amarhisab;

public enum SectionType {
    SELECT("ধরন নির্বাচন করুন"),
    INCOME("আয়"),
    EXPENSE("ব্যায়");


    private final String label;

    SectionType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    // This method returns the enum value as a String for the dropdown
    @Override
    public String toString() {
        return label;
    }

    //Static method to find an enum constant by label
    public static SectionType fromLabel(String label) {
        for (SectionType type : SectionType.values()) {
            if (type.label.equals(label)) {
                return type;
            }
        }
        throw new IllegalArgumentException("No enum constant with label: " + label);
    }


}

