import React from "react";

interface FormInputProps {
    label: string;
    type: string;
    value: string;
    onChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
}

const FormInput = ({label, type, value, onChange}: FormInputProps) => (
    <div className="form-control w-full max-w-xs mb-2">
        <label className="label font-semibold">
            <span className="label-text">{label}</span>
        </label>
        <input
            type={type}
            className="input input-bordered w-full mt-1"
            value={value}
            onChange={onChange}
        />
    </div>
);

export default FormInput;
