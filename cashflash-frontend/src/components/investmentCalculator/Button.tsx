interface ButtonProps {
    onClick: () => void;
    text: string;
}

const Button = ({onClick, text}: ButtonProps) => (
    <button className="btn btn-primary mt-2" onClick={onClick}>
        {text}
    </button>
);

export default Button;
