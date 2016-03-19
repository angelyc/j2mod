/*
 * This file is part of j2mod.
 *
 * j2mod is a fork of the jamod library written by Dieter Wimberger
 * and then further enhanced by Julie Haugh with a new LGPL license
 * and upgraded to Java 1.6
 *
 * j2mod is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * j2mod is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Foobar.  If not, see <http://www.gnu.org/licenses
 */
package com.ghgande.j2mod.modbus.msg;

import com.ghgande.j2mod.modbus.Modbus;
import com.ghgande.j2mod.modbus.util.BitVector;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Class implementing a <tt>ReadCoilsResponse</tt>.
 * The implementation directly correlates with the class 1
 * function <i>read coils (FC 1)</i>. It encapsulates
 * the corresponding response message.
 * <p>
 * Coils are understood as bits that can be manipulated
 * (i.e. set or unset).
 *
 * @author Dieter Wimberger
 * @version 1.2rc1 (09/11/2004)
 */

/**
 * Completed re-implementation 1/10/2011
 *
 * Created getMessage() method to abstractly create the message
 * data.
 * Cleaned up the constructors.
 */
public final class ReadCoilsResponse extends ModbusResponse {
    private BitVector coils;

    /**
     * getBitCount -- return the number of coils
     *
     * @return number of defined coils
     */
    public int getBitCount() {
        if (coils == null) {
            return 0;
        }
        else {
            return coils.size();
        }
    }

    /**
     * getCoils -- get the coils bit vector.
     *
     * The coils vector may be read (when operating as a master) or
     * written (when operating as a slave).
     *
     * @return BitVector containing the coils.
     */
    public BitVector getCoils() {
        return coils;
    }

    /**
     * Convenience method that returns the state
     * of the bit at the given index.
     * <p>
     *
     * @param index the index of the coil for which
     *              the status should be returned.
     *
     * @return true if set, false otherwise.
     *
     * @throws IndexOutOfBoundsException if the
     *                                   index is out of bounds
     */
    public boolean getCoilStatus(int index) throws IndexOutOfBoundsException {

        if (index < 0) {
            throw new IllegalArgumentException(index + " < 0");
        }

        if (index > coils.size()) {
            throw new IndexOutOfBoundsException(index + " > " + coils.size());
        }

        return coils.getBit(index);
    }

    /**
     * Sets the status of the given coil.
     *
     * @param index the index of the coil to be set.
     * @param b     true if to be set, false for reset.
     */
    public void setCoilStatus(int index, boolean b) {
        if (index < 0) {
            throw new IllegalArgumentException(index + " < 0");
        }

        if (index > coils.size()) {
            throw new IndexOutOfBoundsException(index + " > " + coils.size());
        }

        coils.setBit(index, b);
    }

    public void writeData(DataOutput output) throws IOException {
        byte result[] = getMessage();

        output.write(result);
    }

    public void readData(DataInput input) throws IOException {
        int count = input.readUnsignedByte();
        byte[] data = new byte[count];

        input.readFully(data, 0, count);
        coils = BitVector.createBitVector(data);
        setDataLength(count + 1);
    }

    public byte[] getMessage() {
        int len = 1 + coils.byteSize();
        byte result[] = new byte[len];

        result[0] = (byte)coils.byteSize();
        System.arraycopy(coils.getBytes(), 0, result, 1, coils.byteSize());

        return result;
    }

    /**
     * ReadCoilsResponse -- create an empty response message to be
     * filled in later.
     */
    public ReadCoilsResponse() {
        setFunctionCode(Modbus.READ_COILS);
        setDataLength(1);
        coils = null;
    }

    /**
     * ReadCoilsResponse -- create a response for a given number of
     * coils.
     *
     * @param count the number of bits to be read.
     */
    public ReadCoilsResponse(int count) {
        setFunctionCode(Modbus.READ_COILS);
        coils = new BitVector(count);
        setDataLength(coils.byteSize() + 1);
    }
}