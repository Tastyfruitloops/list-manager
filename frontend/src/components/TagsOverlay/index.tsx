import { useEffect, useState } from "react";
import { ItemData, ListData, TagData } from "../../interfaces";
import "./style.scss";

const TagsOverlay = ({
    isOpen,
    setIsOpen,
    opening,
    setOpening,
    list,
    selectedItem,
    onTagClick,
    onTagDelete,
    onNewTag,
}: TagsOverlayProps) => {
    const [closing, setClosing] = useState(false);
    const [tags, setTags] = useState<TagData[]>(list.tags);

    useEffect(() => {
        setTags(list.tags);
    }, [list]);

    if (!isOpen) return null;

    return (
        <div
            className={"tags-overlay" + (opening ? " on-open" : "")}
            onAnimationEnd={(event) => {
                if (event.animationName !== "fade-in") return;
                setOpening(false);
            }}
            onClick={(event) => {
                if (event.target !== event.currentTarget) return;
                setClosing(true);
            }}
        >
            <div
                className={"tags-modal" + (closing ? " on-close" : "")}
                onAnimationEnd={(event) => {
                    if (event.animationName !== "fade-out") {
                        return;
                    }

                    setIsOpen(false);
                    setClosing(false);
                }}
            >
                <div className="header">
                    <h3>Tags</h3>
                    <span
                        className="material-symbols-outlined"
                        onClick={() => setClosing(true)}
                    >
                        close
                    </span>
                </div>
                <input
                    type="text"
                    placeholder="Search tags or add new..."
                    onChange={(event) => {
                        setTags(() => {
                            if (event.target.value === "") {
                                return list.tags;
                            }

                            return list.tags.filter((tag) =>
                                tag.name
                                    .toLowerCase()
                                    .includes(event.target.value.toLowerCase())
                            );
                        });
                    }}
                    onKeyDown={(event) => {
                        if (event.key === "Enter") {
                            const tag = tags.find(
                                (tag) =>
                                    tag.name.toLowerCase() ===
                                    (
                                        event.target as HTMLInputElement
                                    ).value.toLowerCase()
                            );

                            if (tag) {
                                onTagClick(tag);
                                setClosing(true);
                                return;
                            }

                            onNewTag(
                                JSON.stringify({
                                    name: (event.target as HTMLInputElement)
                                        .value,
                                })
                            );
                            setClosing(true);
                        }
                    }}
                />
                <div className="tags-list">
                    {tags.map((tag, index) => (
                        <div
                            className="tag-wrapper"
                            key={index}
                            onClick={() => {
                                setClosing(true);
                                onTagClick(tag);
                            }}
                        >
                            <span className="tag">{tag.name}</span>
                            <span
                                className="material-symbols-outlined"
                                onClick={() => {
                                    setClosing(true);
                                    onTagDelete(tag);
                                }}
                                style={{
                                    display: !selectedItem?.tags.find(
                                        (t) => t.id === tag.id
                                    )
                                        ? "none"
                                        : "block",
                                }}
                            >
                                close
                            </span>
                        </div>
                    ))}
                    {tags.length === 0 && (
                        <span className="no-tags">No tags</span>
                    )}
                </div>
            </div>
        </div>
    );
};

interface TagsOverlayProps {
    isOpen: boolean;
    setIsOpen: (isOpen: boolean) => void;
    opening: boolean;
    setOpening: (opening: boolean) => void;
    list: ListData;
    selectedItem?: ItemData;
    onTagClick: (tag: TagData) => void;
    onTagDelete: (tag: TagData) => void;
    onNewTag: (body: BodyInit) => Promise<void>;
}

export default TagsOverlay;
