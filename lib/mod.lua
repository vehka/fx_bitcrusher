local fx = require("fx/lib/fx")
local mod = require 'core/mods'

local FxBitcrusher = fx:new{
    subpath = "/fx_bitcrusher"
}


function FxBitcrusher:add_params()
    params:add_group("fx_bitcrusher", "FX Bitcrusher", 7)
    FxBitcrusher:add_slot("fx_bitcrusher_slot", "slot")
    FxBitcrusher:add_control("fx_bitcrusher_bitrate", "bitrate", "bitrate", controlspec.new(2, 32, 'lin', 0, 12))
    FxBitcrusher:add_control("fx_bitcrusher_samplerate", "samplerate", "samplerate", controlspec.new(1000, 48000, 'lin', 1000, 48000))
    FxBitcrusher:add_control("fx_bitcrusher_tone", "tone", "tone", controlspec.new(0, 1, 'lin', 0, 0.1))
    FxBitcrusher:add_control("fx_bitcrusher_gate", "gate", "gate", controlspec.new(0, 1, 'lin', 0, 0.5))
    FxBitcrusher:add_control("fx_bitcrusher_gain", "gain", "gain", controlspec.new(0, 1, 'lin', 0, 0.1))
end

mod.hook.register("script_pre_init", "bitcrusher mod pre init", function()
    FxBitcrusher:install()
end)

mod.hook.register("script_post_cleanup", "bitcrusher mod post cleanup", function()
end)

return FxBitcrusher
