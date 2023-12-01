import { useEffect, useState } from "react";
import { Badge, Item, TagsOverlay } from "..";
import { ItemData, ListData, TagData, UserData } from "../../interfaces";
import "./style.scss";

interface ItemWrapper {
    id: number;
    uuid?: string;
    name: string;
    tags?: TagData[];
    item: JSX.Element;
}

const List = ({
    list,
    user,
    onListUpdate: onListUpdate,
    query,
}: ContentProps) => {
    const [items, setItems] = useState<ItemWrapper[]>([]);
    const [itemsCount, setItemsCount] = useState<number>(0);

    const [name, setName] = useState<string>(list.name);
    const [publicList, setPublicList] = useState<boolean>(list.public);
    const [archived, setArchived] = useState<boolean>(list.archived);
    const [owned, setOwned] = useState<boolean>(false);
    const [ownerUsername, setOwnerUsername] = useState<string>("");

    const [isOpen, setIsOpen] = useState<boolean>(false);
    const [opening, setOpening] = useState<boolean>(false);
    const [selectedItem, setSelectedItem] = useState<ItemData>();

    useEffect(() => {
        setName(list.name);
        setPublicList(list.public);
        setArchived(list.archived);
        setOwned(list.ownerId == user?.id);

        if (!owned) {
            fetch(`${import.meta.env.VITE_API_URL}/users/${list.ownerId}`, {
                method: "GET",
                credentials: "include",
                mode: "cors",
            })
                .then((res) => {
                    if (res.status === 200) {
                        return res.json();
                    }

                    throw new Error("Not logged in.");
                })
                .then((res) => setOwnerUsername(res.username))
                .catch(() => setOwnerUsername("Someone"));
        }

        setItems(
            list.items.map((item) => {
                setItemsCount((itemsCount) => itemsCount + 1);
                return {
                    id: itemsCount,
                    uuid: item.id,
                    name: item.name,
                    tags: item.tags,
                    item: (
                        <Item
                            item={item}
                            viewOnly={!owned || list.archived}
                            key={item.id}
                            onDelete={async () => await removeItem(item.id)}
                            onEdit={(name) => {
                                updateItem(
                                    item.id,
                                    JSON.stringify({ name: name })
                                );
                            }}
                            onTagsClick={() => {
                                setSelectedItem(item);
                                setIsOpen(true);
                                setOpening(true);
                            }}
                        />
                    ),
                };
            })
        );
    }, [list, user]);

    useEffect(() => {
        if (query) {
            const items = list.items.filter((item) =>
                item.name.toLowerCase().includes(query.toLowerCase())
            );

            setItems(
                items.map((item) => {
                    setItemsCount((itemsCount) => itemsCount + 1);
                    return {
                        id: itemsCount,
                        uuid: item.id,
                        name: item.name,
                        tags: item.tags,
                        item: (
                            <Item
                                item={item}
                                viewOnly={!owned || list.archived}
                                key={item.id}
                                onDelete={async () => await removeItem(item.id)}
                                onEdit={(name) => {
                                    updateItem(
                                        item.id,
                                        JSON.stringify({ name: name })
                                    );
                                }}
                                onTagsClick={() => {
                                    setSelectedItem(item);
                                    setIsOpen(true);
                                    setOpening(true);
                                }}
                            />
                        ),
                    };
                })
            );
        } else {
            setItems(
                list.items.map((item) => {
                    setItemsCount((itemsCount) => itemsCount + 1);
                    return {
                        id: itemsCount,
                        uuid: item.id,
                        name: item.name,
                        tags: item.tags,
                        item: (
                            <Item
                                item={item}
                                viewOnly={!owned || list.archived}
                                key={item.id}
                                onDelete={async () => await removeItem(item.id)}
                                onEdit={(name) => {
                                    updateItem(
                                        item.id,
                                        JSON.stringify({ name: name })
                                    );
                                }}
                                onTagsClick={() => {
                                    setSelectedItem(item);
                                    setIsOpen(true);
                                    setOpening(true);
                                }}
                            />
                        ),
                    };
                })
            );
        }
    }, [query]);

    const removeItem = async (itemId: string) => {
        const res = await fetch(
            `${import.meta.env.VITE_API_URL}/lists/item/${itemId}`,
            {
                method: "DELETE",
                credentials: "include",
                mode: "cors",
            }
        );

        if (res.status === 204) {
            onListUpdate();
        }
    };

    const updateItem = async (itemId: string, body: BodyInit) => {
        const res = await fetch(
            `${import.meta.env.VITE_API_URL}/lists/item/${itemId}`,
            {
                method: "PUT",
                credentials: "include",
                mode: "cors",
                headers: {
                    "Content-Type": "application/json",
                },
                body: body,
            }
        );

        if (res.status === 200) {
            onListUpdate();
        }
    };

    const addItem = async () => {
        const res = await fetch(
            `${import.meta.env.VITE_API_URL}/lists/${list.id}/item`,
            {
                method: "POST",
                credentials: "include",
                mode: "cors",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    name: "New item",
                    tagIds: [],
                }),
            }
        );

        if (res.status === 201) {
            setItemsCount((itemsCount) => itemsCount + 1);
            const item = await res.json();
            setItems((items) => {
                return [
                    ...items,
                    {
                        id: itemsCount,
                        uuid: item.id,
                        name: item.name,
                        tags: item.tags,
                        item: (
                            <Item
                                item={item}
                                viewOnly={!owned || list.archived}
                                key={item.id}
                                onDelete={async () => await removeItem(item.id)}
                                onEdit={(name) => {
                                    updateItem(
                                        item.id,
                                        JSON.stringify({ name: name })
                                    );
                                }}
                                onTagsClick={() => {
                                    setSelectedItem(item);
                                    setIsOpen(true);
                                    setOpening(true);
                                }}
                            />
                        ),
                    },
                ];
            });
        }
    };

    const onListRemove = async () => {
        const res = await fetch(
            `${import.meta.env.VITE_API_URL}/lists/${list.id}`,
            {
                method: "DELETE",
                credentials: "include",
                mode: "cors",
            }
        );

        if (res.status === 204) {
            onListUpdate();
        }
    };

    if (!owned && !list.public)
        return (
            <div className="content invalid-list">
                <h1>ಠಿ_ಠ</h1>
                <p>
                    You don't have access to this list. Ask the owner to share!
                </p>
            </div>
        );

    const updateList = async (body: BodyInit) => {
        const res = await fetch(
            `${import.meta.env.VITE_API_URL}/lists/${list.id}`,
            {
                method: "PUT",
                credentials: "include",
                mode: "cors",
                headers: {
                    "Content-Type": "application/json",
                },
                body: body,
            }
        );

        if (res.status === 200) {
            onListUpdate();
        }
    };

    const updateItemTags = async (body: BodyInit) => {
        if (!selectedItem) {
            return;
        }

        const res = await fetch(
            `${import.meta.env.VITE_API_URL}/lists/item/${selectedItem?.id}`,
            {
                method: "PUT",
                credentials: "include",
                mode: "cors",
                headers: {
                    "Content-Type": "application/json",
                },
                body: body,
            }
        );

        if (res.status === 200 || res.status === 204) {
            onListUpdate();
        }

        setSelectedItem(undefined);
    };

    return (
        <div className="content">
            <section className="header">
                <div className="path">
                    <Badge>
                        <span className="material-symbols-outlined">
                            person
                        </span>
                        {owned ? "You" : ownerUsername}
                    </Badge>
                    /
                    <Badge className="list-badge">
                        <span className="material-symbols-outlined">notes</span>
                        {list.name ?? "New list"}
                    </Badge>
                </div>
                <div className="actions">
                    <button
                        className="archive"
                        disabled={!owned}
                        onClick={async () => {
                            await updateList(
                                JSON.stringify({ archived: !archived })
                            );
                            setArchived(!archived);
                        }}
                    >
                        <span className="material-symbols-outlined">
                            {list.archived ? "unarchive" : "archive"}
                        </span>
                        {list.archived ? "Unarchive" : "Archive"}
                    </button>
                    <button
                        className="share"
                        disabled={!owned}
                        onClick={async () => {
                            await updateList(
                                JSON.stringify({ public: !publicList })
                            );
                            setPublicList(!publicList);
                        }}
                    >
                        <span className="material-symbols-outlined">
                            {list.public ? "visibility_off" : "visibility"}
                        </span>
                        {list.public ? "Make private" : "Make public"}
                    </button>
                    <button
                        className="delete"
                        disabled={!owned}
                        onClick={onListRemove}
                    >
                        <span className="material-symbols-outlined">
                            delete
                        </span>
                        Delete
                    </button>
                </div>
            </section>
            <section className="info">
                <h1
                    contentEditable={owned && !list.archived}
                    data-placeholder="Untitled"
                    onBlur={(event) => {
                        if (event.currentTarget.textContent === name) return;
                        if (event.currentTarget.textContent === "") return;

                        updateList(
                            JSON.stringify({
                                name: event.currentTarget.textContent,
                            })
                        );
                        setName(event.currentTarget.textContent ?? name);
                    }}
                    dangerouslySetInnerHTML={{ __html: name }}
                ></h1>
                <div className="badges">
                    {list.public ? (
                        <Badge className="status">
                            <span className="material-symbols-outlined">
                                visibility
                            </span>
                            Public
                        </Badge>
                    ) : (
                        <Badge>
                            <span className="material-symbols-outlined">
                                visibility_off
                            </span>
                            Private
                        </Badge>
                    )}
                    {list.archived && (
                        <Badge>
                            <span className="material-symbols-outlined">
                                archive
                            </span>
                            Archived
                        </Badge>
                    )}
                </div>
            </section>
            <section className="container">
                {items.map((item) => item.item)}
                {items.length === 0 && (
                    <span className="empty">No items yet</span>
                )}
                <button
                    className="add-item"
                    disabled={!owned || list.archived}
                    onClick={addItem}
                    style={{ display: owned ? "flex" : "none" }}
                >
                    <span className="material-symbols-outlined">add</span>
                    Add
                </button>
            </section>
            <TagsOverlay
                isOpen={isOpen}
                setIsOpen={setIsOpen}
                opening={opening}
                setOpening={setOpening}
                list={list}
                selectedItem={selectedItem}
                onTagClick={async (tag) => {
                    if (!selectedItem) return;
                    if (selectedItem.tags.find((t) => t.id === tag.id)) return;

                    await updateItemTags(
                        JSON.stringify({
                            tagIds: [
                                ...(selectedItem?.tags.map((tag) => tag.id) ??
                                    []),
                                tag.id,
                            ],
                        })
                    );
                }}
                onTagDelete={async (tag) => {
                    if (!selectedItem) return;
                    if (!selectedItem.tags.find((t) => t.id === tag.id)) return;

                    await updateItemTags(
                        JSON.stringify({
                            tagIds: [
                                ...(selectedItem?.tags.map((tag) => tag.id) ??
                                    []),
                            ].filter((id) => id !== tag.id),
                        })
                    );
                }}
                onNewTag={async (body) => {
                    if (!selectedItem) return;

                    const res = await fetch(
                        `${import.meta.env.VITE_API_URL}/lists/${list.id}/tag`,
                        {
                            method: "POST",
                            credentials: "include",
                            mode: "cors",
                            headers: {
                                "Content-Type": "application/json",
                            },
                            body: body,
                        }
                    );

                    if (res.status === 201) {
                        const tag = await res.json();
                        await updateItemTags(
                            JSON.stringify({
                                tagIds: [
                                    ...(selectedItem?.tags.map(
                                        (tag) => tag.id
                                    ) ?? []),
                                    tag.id,
                                ],
                            })
                        );
                    }
                }}
            />
        </div>
    );
};

interface ContentProps {
    user?: UserData;
    list: ListData;
    onListUpdate: () => void;
    query?: string;
}

export default List;
