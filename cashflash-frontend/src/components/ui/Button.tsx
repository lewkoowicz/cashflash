import React from "react";

interface ButtonProps {
    className: string;
    onClick: () => void;
    text: string;
    children?: React.ReactNode;
    disabled?: boolean;
}

const Button = ({className, onClick, text, children, disabled}: ButtonProps) => (
    <button className={`btn ${className}`} onClick={onClick} disabled={disabled}>
        {text}
        {children}
    </button>
);

export default Button;
