Uses Unimixins.
## Features
``/enchant`` and ``/effect`` can specify using the (internal) name instead of just number now, also roman numerals for level.

``/enchant clear`` to remove all enchants

``/enchant (existing enchantment) (new number incl. 0)`` --changes or removes it rather than complaining it's incompatible with itself.

Two little give commands from alpha (/iron and /wood).

``/loot``, with a ``dump`` option for output that just sends resulting itemstacks to the logger, and a leading ``times`` option to repeat itself N times (though it still doesn't know how to stack items, so insert with more times isn't gonna be very useful. (have not implemented ``/loot mine``, or tool checking, but all the other vanilla options are in.)
