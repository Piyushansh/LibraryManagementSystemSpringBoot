package com.librarymanagement.dto;

import lombok.*;

/**
 * =====================================================
 * IssueRequest DTO - Used when issuing a book
 * =====================================================
 * When admin issues a book, they provide:
 *   - userId  → which user is getting the book
 *   - bookId  → which book is being issued
 * =====================================================
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IssueRequest {

    private Long userId;
    private Long bookId;
}
