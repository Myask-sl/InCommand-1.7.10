package invalid.myask.incommand.ducks;

public interface IMobLooter {
    void commandLoot$fewDrop(boolean PK, int looting);
    void commandLoot$equipDrop(boolean PK, int looting);
    void commandLoot$rareDrop(int bonus);

    void commandLoot$clearEquipment();
    void commandLoot$armorUp();
    void commandLoot$enchantUp();
}
