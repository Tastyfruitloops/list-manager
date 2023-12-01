import { useState } from "react";
import { Badge } from "..";
import { ItemData } from "../../interfaces";
import "./style.scss";

const Item = ({ item, viewOnly, onDelete, onEdit, onTagsClick }: ItemProps) => {
    const [name, setName] = useState(item.name ?? "New item");

    return (
        <div className="item">
            <section>
                <input
                    value={name}
                    onChange={(event) => {
                        if (viewOnly) return;
                        setName(event.target.value);
                    }}
                    onBlur={() => {
                        if (viewOnly) return;
                        onEdit(name);
                    }}
                />

                <div
                    className="tags"
                    onClick={!viewOnly ? onTagsClick : undefined}
                    style={{
                        display:
                            viewOnly && item.tags.length == 0 ? "none" : "flex",
                    }}
                >
                    {item.tags.map((tag) => (
                        <Badge key={tag.id}>
                            <span className="tag">{tag.name}</span>
                        </Badge>
                    ))}
                    {item.tags.length == 0 && !viewOnly && (
                        <Badge className="ghost">
                            <span className="material-symbols-outlined">
                                add
                            </span>
                            <span className="tag">Add tags</span>
                        </Badge>
                    )}
                </div>
            </section>
            <span
                className="material-symbols-outlined"
                onClick={onDelete}
                style={{
                    visibility: viewOnly ? "hidden" : "visible",
                }}
            >
                delete
            </span>
        </div>
    );
};

interface ItemProps {
    item: ItemData;
    viewOnly?: boolean;
    onDelete: () => void;
    onEdit: (name: string) => void;
    onTagsClick: () => void;
}

export default Item;
