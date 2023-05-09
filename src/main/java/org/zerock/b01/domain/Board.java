package org.zerock.b01.domain;

import lombok.*;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "imageSet")
public class Board extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno;

    @Column(length = 500, nullable = false) //컬럼의 길이와 null허용여부
    private String title;

    @Column(length = 2000, nullable = false)
    private String content;

    @Column(length = 50, nullable = false)
    private String writer;

    @OneToMany(mappedBy = "board", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY,orphanRemoval = true)
    @Builder.Default
    @BatchSize(size = 20) //N+1은 엄청난 양의 db를 처리하기 때문에 사용 
    private Set<BoardImage> imageSet = new HashSet<>();

    public void addImage(String uuid, String fileName){
        BoardImage boardImage = BoardImage.builder()
                .uuid(uuid).fileName(fileName).board(this).ord(imageSet.size()).build();
                imageSet.add(boardImage);
    }


    public void clearImages(){
        imageSet.forEach(boardImage -> boardImage.changeBoard(null));
        this.imageSet.clear();
    }
    public void change(String title, String content){
        this.title =title;
        this.content=content;
    }

}
