package dto;

public class ItemListDto {
    private String name;
    private Long boardId;

    public ItemListDto(String name, Long boardId) {
        this.name = name;
        this.boardId = boardId;
    }

    public Long getBoardId() {
        return boardId;
    }

    public void setBoardId(Long boardId) {
        this.boardId = boardId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
