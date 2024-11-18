package com.eco.ecosystem.controllers.requestBodies;

import com.eco.ecosystem.game.board.Slot;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PutRabbitCardAndSwapTwoRequestBody {
    Slot rabbitSlot;
    Slot slotToSwap1;
    Slot slotToSwap2;
}
