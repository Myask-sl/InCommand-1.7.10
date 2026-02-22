Uses Unimixins.
## Features

### Command Parsing Enhancement
``/enchant`` and ``/effect`` can specify using the (internal) name instead of just number now, also roman numerals for level.

``/enchant clear`` to remove all enchants

``/enchant (existing enchantment) (new number incl. 0)`` --changes or removes it rather than complaining it's incompatible with itself.

``/coordtest (x y z)`` for testing out ~/^ coordinates.

### Back/Forward ports

Two little give commands from alpha (``/iron`` and ``/wood``). As originally implemented: they each have a recharge time.

``/loot``, with a ``dump`` option for output that just sends resulting itemstacks to the logger, and a leading ``times`` option to repeat itself N times (though it still doesn't know how to stack items, so insert with more times isn't gonna be very useful. (have not implemented ``/loot mine``, or tool checking, but all the other vanilla options are in.)

``/killother``, with optional override for ``/kill`` among other aliasing options. ``/die`` kept separate as it being lower permission level made sense.

``/rotate`` for your entity-rotating needs (supports caret-coordinate and rotation-tilde syntax!), ``/rotateself`` for if you want people to be able to rotate self at lower permission.

``/fill`` for mass-block-setting needs from 1.8, including the 1.21.5 enhancements to replace mode. ``max_block_modifications`` gamerule added accordingly; and also ``max_block_meta`` gamerule for altering top end when a meta-ID-extender is in place. NBT writing/tag filtering Not Yet Impl.

~~``/clone``~~

``/warp`` for dimension change from April Fools' Day, but with a target. (*Caveat emptor*, a bit buggy.)

Limited support for ``@s`` selector in e.g. enchant/effect, new commands.
