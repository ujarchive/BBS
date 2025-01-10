package com.example.demo.Domain;

import com.example.demo.Common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "menu")
@Getter
@NoArgsConstructor
public class Menu extends BaseTimeEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(nullable = true, length = 255)
    private String path;

    @ElementCollection(targetClass = UserRole.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "menu_roles", joinColumns = @JoinColumn(name = "menu_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Set<UserRole> roles = new HashSet<>(); // 접근 가능한 역할 목록


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id") // 계층 구조를 위한 상위 메뉴
    private Menu parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Menu> children = new ArrayList<>();

    @Column(nullable = false)
    private int menuOrder;

    @Column(nullable = true)
    private boolean isEnabled;

    @Builder
    public Menu(String name, String path, Set<UserRole> roles, Menu parent, List<Menu> children, int menuOrder, boolean isEnabled) {
        this.name = name;
        this.path = path;
        this.roles = roles;
        this.parent = parent;
        this.children = children;
        this.menuOrder = menuOrder;
        this.isEnabled = isEnabled;
    }
}