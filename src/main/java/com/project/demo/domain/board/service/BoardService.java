package com.project.demo.domain.board.service;

import com.project.demo.domain.board.dto.BoardDTO;
import com.project.demo.domain.board.entity.Board;
import com.project.demo.domain.board.repository.BoardRepository;
import com.project.demo.domain.members.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    public void save(BoardDTO boardDTO) {
        Board board = createBoardEntity(boardDTO);
        board.getWriter().addBoard(board);
        boardRepository.save(board);
    }

    // 기존 List<Board>값으로 넘어가지만 페이징 설정을 해주면 Page<Board>로 넘어간다.
    public Page<Board> getBoardList(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }

    // createBoardDTO 메소드 끌어와서 board -> boardDTO 매핑된 Page 반환
    public Page<BoardDTO> getBoardListByWhiskeyId(Long id, Pageable pageable) {
        return boardRepository.findAllByWhiskeyId(id, pageable)
                .map(this::createBoardDTO);
    }

    public Page<BoardDTO> getBoardListByWhiskeyIdAndWriter(Long id, String writer, Pageable pageable) {
        return boardRepository.findAllByWhiskeyIdAndWriter_UserId(id, writer, pageable)
                .map(this::createBoardDTO);
    }

    public BoardDTO findBoardById(Long id) throws Exception {
        Board board = boardRepository.findById(id).orElse(null);
        if (board == null) {
            throw new Exception("해당 게시글이 존재하지 않습니다.");
        }
        return createBoardDTO(board);
    }

    public Long deleteBoardById(Long id) {

        Board board = boardRepository.findById(id).orElse(null);
        assert board != null;

        board.getWriter().removeBoard(board);
        Long whiskeyId = board.getWhiskeyId();
        boardRepository.delete(board);

        return whiskeyId;
    }

    @Transactional
    public Long updateBoard(Long id, BoardDTO boardDTO) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "해당 게시글이 존재하지 않습니다. ID: " + id));

        board.update(boardDTO.getTitle(), boardDTO.getContent());
        return board.getWhiskeyId();
    }

    // DTO -> Entity 변환
    private Board createBoardEntity(BoardDTO boardDTO) {

        return Board.builder()
                .writer(memberRepository.findByUserId(boardDTO.getWriter()))
                .whiskeyId(boardDTO.getWhiskeyId())
                .title(boardDTO.getTitle())
                .regDate(boardDTO.getRegDate())
                .content(boardDTO.getContent())
                .build();
    }

    // DTO -> Entity 생성
    private BoardDTO createBoardDTO(Board board) {

        return BoardDTO.builder()
                .id(board.getId())
                .writer(board.getWriter().getUserId())
                .whiskeyId(board.getWhiskeyId())
                .title(board.getTitle())
                .regDate(board.getRegDate())
                .content(board.getContent())
                .build();
    }
}
