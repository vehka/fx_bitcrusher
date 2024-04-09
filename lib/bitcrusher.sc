FxBitcrusher : FxBase {

    *new { 
        var ret = super.newCopyArgs(nil, \none, (
            bitrate: 12,
            samplerate: 48000,
            tone: 0.5,
            gate: 0.5,
            gain: 1.0
        ), nil, 1);
        ^ret;
    }

    *initClass {
        FxSetup.register(this.new);
    }

    subPath {
        ^"/fx_bitcrusher";
    }  

    symbol {
        ^\fxBitcrusher;
    }

    addSynthdefs {
        SynthDef(\fxBitcrusher, {|inBus, outBus, bitrate=12, samplerate=48000, tone=0.5, gate=0.5, gain=1.0|
            var bc_wet = In.ar(inBus, 2);
            var freq, filterType;
            
            // First we feed into a HPF to filter out sub-20Hz
            bc_wet = HPF.ar(bc_wet, 25);
            // Then into a noise gate
            gate = Select.kr(gate > 0.5, [
              LinExp.kr(gate, 0, 0.5, 0.001, 0.015),
              LinExp.kr(gate, 0.5, 1, 0.015, 0.05),
            ]);
            bc_wet = Compander.ar(bc_wet, bc_wet, gate, 6, 1, 0.1, 0.01);
            // Then we LPF to prevent aliasing before bit-reducing
            bc_wet = LPF.ar(bc_wet, 5.66.reciprocal * samplerate);
            // Then into a bit reducer
            bc_wet = Decimator.ar(bc_wet, samplerate, bitrate);

            // Then we feed into the Tone section
            // Tone controls a MMF, exponentially ranging from 10 Hz - 21 kHz
            // Tone above 0.75 switches to a HPF
            freq = Select.kr(tone > 0.75, [
              Select.kr(tone > 0.2, [
                LinExp.kr(tone, 0, 0.2, 10, 400),
                LinExp.kr(tone, 0.2, 0.75, 400, 20000),
              ]),
              LinExp.kr(tone, 0.75, 1, 20, 21000),
            ]);
            filterType = Select.kr(tone > 0.75, [0, 1]);
            bc_wet = DFM1.ar(
              bc_wet,
              freq,
              \res.kr(0.1),
              gain,
              filterType,
              \noise.kr(0.0003)
            ).softclip;

            // Output to the bus
            Out.ar(outBus, bc_wet);
        }).add;
    }

}