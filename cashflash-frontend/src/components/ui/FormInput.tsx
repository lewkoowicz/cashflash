import React from "react";

interface FormInputProps {
    label: string;
    type: string;
    placeholder: string;
    value: string;
    onChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
}

const FormInput = ({label, type, placeholder, value, onChange}: FormInputProps) => (
    <div className="form-control w-full max-w-xs mb-2">
        <label className="label font-semibold">
            <span className="label-text">{label}</span>
        </label>
        <input
            className="input input-bordered w-full mt-1"
            type={type}
            placeholder={placeholder}
            value={value}
            onChange={onChange}
        />
    </div>
);

export default FormInput;
