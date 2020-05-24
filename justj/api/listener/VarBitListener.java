package scripts.justj.api.listener;

import org.tribot.api2007.types.RSVarBit;

public interface VarBitListener {

  void onVarbitChanged(RSVarBit varbit, Integer newValue);

}
