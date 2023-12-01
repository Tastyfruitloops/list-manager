import { FC, PropsWithChildren } from "react";
import "./style.scss";

const Badge: FC<BadgeProps> = ({
    contentEditable,
    className,
    children,
}: BadgeProps) => {
    return (
        <div
            contentEditable={contentEditable}
            className={"badge " + (className ?? "")}
        >
            {children}
        </div>
    );
};

interface BadgeProps extends PropsWithChildren {
    contentEditable?: boolean;
    className?: string;
}

export default Badge;
