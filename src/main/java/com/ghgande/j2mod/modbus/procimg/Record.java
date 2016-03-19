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
package com.ghgande.j2mod.modbus.procimg;

/**
 * @author Julie
 *
 *         File -- an abstraction of a Modbus Record, as supported by the
 *         READ FILE RECORD and WRITE FILE RECORD commands.
 */
public class Record {
    private int m_Record_Number;
    private int m_Register_Count;
    private Register m_Registers[];

    public int getRecordNumber() {
        return m_Record_Number;
    }

    public int getRegisterCount() {
        return m_Register_Count;
    }

    public Register getRegister(int register) {
        if (register < 0 || register >= m_Register_Count) {
            throw new IllegalAddressException();
        }

        return m_Registers[register];
    }

    public Record setRegister(int ref, Register register) {
        if (ref < 0 || ref >= m_Register_Count) {
            throw new IllegalAddressException();
        }

        m_Registers[ref] = register;

        return this;
    }

    public Record(int recordNumber, int registers) {
        m_Record_Number = recordNumber;
        m_Register_Count = registers;
        m_Registers = new Register[registers];

        for (int i = 0; i < m_Register_Count; i++) {
            m_Registers[i] = new SimpleRegister(0);
        }
    }
}